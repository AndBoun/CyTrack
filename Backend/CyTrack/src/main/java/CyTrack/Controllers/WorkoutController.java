package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Services.BadgeService;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;
import CyTrack.Sockets.LeaderBoardSocket;
import CyTrack.Sockets.WorkoutTrackingSocket;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Tag(name = "Workout Controller", description = "REST APIs related to Workout")
@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;
    private final BadgeService badgeService;
    //Quick pipeline test
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    private final WorkoutTrackingSocket workoutTrackingSocket;

    //Constructor
    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService,
                             BadgeService badgeService,
                             WorkoutTrackingSocket workoutTrackingSocket) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.badgeService = badgeService;
        this.workoutTrackingSocket = workoutTrackingSocket;
    }

    //Create a workout
    @Operation(
            summary = "Create a Workout",
            description = "Create a new Workout without 'starting' the particular workout. Useful for testing.",
            responses = {
                   @ApiResponse (
                           responseCode = "201",
                           description = "Workout created",
                           content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))
                   ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "userID",
                    description = "ID of the user to create a workout for",
                    required = true,
                    example = "1"
            ),
            @Parameter(
                    name = "workout",
                    description = "JSON body representing details for a workout to be created",
                    required = true,
                    schema = @Schema(implementation = Workout.class)
            )
    })
    @PostMapping("/{userID}/workout")
    public ResponseEntity<?> createWorkout(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            workout.setUser(user.get());
            Workout newWorkout = workoutService.createWorkout(workout);

            //award badges
            badgeService.awardEligibleBadges(user.get());

            //Notify leaderboard socket about potential update in total time
            LeaderBoardSocket.updateLeaderboard(user.get().getUserID());

            WorkoutIDResponse response = new WorkoutIDResponse("success", newWorkout.getWorkoutID(), "Workout created");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    //create and start workout
    @Operation(
            summary = "Create and start a Workout",
            description = "Create a new Workout and 'start' the workout timer to update time logs",
            responses = {
                    @ApiResponse (
                            responseCode = "201",
                            description = "Workout created and started",
                            content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))
                    ),
                    @ApiResponse (
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))
                    ),
                    @ApiResponse (
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "userID",
                    description = "ID of the user to create and start a workout for",
                    required = true,
                    example = "1"
            ),
            @Parameter(
                    name = "workout",
                    description = "JSON body representing details for a workout to be created and started",
                    required = true,
                    schema = @Schema(implementation = Workout.class)
            )
    })
    @PostMapping("/{userID}/createAndStart")
    public ResponseEntity<?> createAndStart(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            workout.setUser(user.get());

            //create workout
            Workout newWorkout = workoutService.createWorkout(workout);

            //start workout and initialize timer (simple)
            Workout startedWorkout = workoutService.startWorkout(newWorkout.getWorkoutID());

            try {
                // Notify WebSocket about workout start
                workoutTrackingSocket.notifyWorkoutStarted(userID);
            } catch (IOException e) {
                ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Failed to notify workout start");
                return ResponseEntity.status(500).body(response);
            }

            badgeService.awardEligibleBadges(user.get());

            WorkoutIDResponse response = new WorkoutIDResponse("success", startedWorkout.getWorkoutID(), "workout created and started");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    // Start a workout
    @Operation(
            summary = "Start a Workout",
            description = "Start an existing workout for a given user and workout ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout started", content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User or workout not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "workoutID", description = "ID of the workout to start", required = true, example = "1")
    })
    @PostMapping("/{userID}/workout/{workoutID}/start")
    public ResponseEntity<?> startWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
            if (workout.isPresent()) {
                Workout startedWorkout = workoutService.startWorkout(workoutID);
                WorkoutIDResponse response = new WorkoutIDResponse("success", workoutID, "Workout started");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    // End a workout
    @Operation(
            summary = "End a Workout",
            description = "End an existing workout for a given user and workout ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout ended", content = @Content(schema = @Schema(implementation = WorkoutIDResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User or workout not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "workoutID", description = "ID of the workout to end", required = true, example = "1")
    })
    @PostMapping("/{userID}/workout/{workoutID}/end")
    public ResponseEntity<?> endWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
            if (workout.isPresent()) {
                Workout endedWorkout = workoutService.endWorkout(workoutID);

                //notify websocket about workout end
                try {
                    // Notify WebSocket about workout end
                    workoutTrackingSocket.notifyWorkoutEnded(userID);
                } catch (IOException e) {
                    ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Failed to notify workout end");
                    return ResponseEntity.status(500).body(response);
                }

                LeaderBoardSocket.updateLeaderboard(user.get().getUserID());

                WorkoutIDResponse response = new WorkoutIDResponse("success", workoutID, "Workout ended");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    //List workouts by user ID
    @Operation(
            summary = "List Workouts by User ID",
            description = "Get all workouts for a specified user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workouts found", content = @Content(schema = @Schema(implementation = WorkoutResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameter(name = "userID", description = "ID of the user to retrieve workouts for", required = true, example = "1")
    @GetMapping("/{userID}/workout")
    public ResponseEntity<?> getAllWorkoutsByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Workout> workouts = workoutService.getAllWorkouts(userID);
            List<WorkoutResponse.WorkoutData> workoutDataList = workouts.stream()
                    .map(workout -> new WorkoutResponse.WorkoutData(
                            workout.getExerciseType(),
                            workout.getDuration(),
                            workout.getCalories(),
                            workout.getDate(),
                            workout.getWorkoutID()
                    ))
                    .toList();
            WorkoutResponse response = new WorkoutResponse("success", workoutDataList, "Workouts found");
            return ResponseEntity.status(200).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }


    //Get a workout by user ID and workout ID
    @Operation(
            summary = "Get Workout by User ID and Workout ID",
            description = "Retrieve a specific workout by user ID and workout ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workout found", content = @Content(schema = @Schema(implementation = WorkoutResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User or workout not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "workoutID", description = "ID of the workout", required = true, example = "1")
    })
    @GetMapping("/{userID}/workout/{workoutID}")
    public ResponseEntity<?> getWorkoutByUserIDAndWorkoutID(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
            if (workout.isPresent()) {
                Workout foundWorkout = workout.get();
                WorkoutResponse.WorkoutData workoutData = new WorkoutResponse.WorkoutData(
                        foundWorkout.getExerciseType(),
                        foundWorkout.getDuration(),
                        foundWorkout.getCalories(),
                        foundWorkout.getDate(),
                        foundWorkout.getWorkoutID()
                );
                WorkoutResponse response = new WorkoutResponse("success", List.of(workoutData), "Workout found");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    //List workouts for a specific user at a specific date
    @Operation(
            summary = "List Workouts by Date",
            description = "Get all workouts for a specified user on a specific date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workouts found", content = @Content(schema = @Schema(implementation = WorkoutResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid date format", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "date", description = "Date in MM-DD-YYYY format", required = true, example = "11-13-2024")
    })
    @GetMapping("/{userID}/workoutByDate/{date}")
    public ResponseEntity<?> getWorkoutsByDate(@PathVariable Long userID,
                                               @PathVariable String date) {
        // Parse the date from "MM-DD-YYYY" format to LocalDate
        LocalDate parsedDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            parsedDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid date format", "Date format must be MM-DD-YYYY");
            return ResponseEntity.status(400).body(response);
        }

        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Workout> workouts = workoutService.getWorkoutsByUserIDAndDate(userID, parsedDate);
            List<WorkoutResponse.WorkoutData> workoutDataList = workouts.stream()
                    .map(workout -> new WorkoutResponse.WorkoutData(
                            workout.getExerciseType(),
                            workout.getDuration(),
                            workout.getCalories(),
                            workout.getDate(),
                            workout.getWorkoutID()
                    ))
                    .toList();
            WorkoutResponse response = new WorkoutResponse("success", workoutDataList, "Workouts found for " + date);
            return ResponseEntity.status(200).body(response);
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    //Get total calories burned for a given date
    @Operation(
            summary = "Get Total Calories Burned by Date",
            description = "Get the total calories burned based on workouts for a specified user on a specific date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Total calories burned found", content = @Content(schema = @Schema(implementation = WorkoutSummaryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid date format", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "date", description = "Date in MM-DD-YYYY format", required = true, example = "12-31-2023")
    })
    @GetMapping("/{userID}/totalCalories/{date}")
    public ResponseEntity<?> getTotalCaloriesByDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            try {
                LocalDate localDate = LocalDate.parse(date, dateFormatter);
                int totalCalories = workoutService.getCaloriesByDate(userID, localDate);
                int totalWorkoutTime = workoutService.getWorkoutTimeByDate(userID, localDate);
                WorkoutSummaryResponse response = new WorkoutSummaryResponse("success", totalCalories, totalWorkoutTime, date, "Total calories burned for " + date);
                return ResponseEntity.ok(response);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("error", 400, "Invalid date format", "Please use MM-DD-YYYY format for the date"));
            }
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "User not found"));
        }
    }

    //Get total workout time for a given date
    @Operation(
            summary = "Get total workout time by date",
            description = "Retrieves the total workout time in minutes for a specific user on a given date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Total workout time retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutSummaryResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid date format",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "date", description = "Date in MM-DD-YYYY format", required = true, example = "12-31-2023")
    })
    @GetMapping("/{userID}/totalWorkoutTime/{date}")
    public ResponseEntity<?> getTotalWorkoutTimeByDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            try {
                LocalDate localDate = LocalDate.parse(date, dateFormatter);
                int totalWorkoutTime = workoutService.getWorkoutTimeByDate(userID, localDate);
                WorkoutSummaryResponse response = new WorkoutSummaryResponse("success", 0, totalWorkoutTime, date, "Total workout time for " + date);
                return ResponseEntity.ok(response);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse("error", 400, "Invalid date format", "Please use MM-DD-YYYY format for the date"));
            }
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "User not found"));
        }
    }

    //Update a workout
    @Operation(
            summary = "Update workout details",
            description = "Updates details of a specific workout for a given user. Allows updating of exercise type, duration, calories, and date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout updated successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutIDResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or workout not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "workoutID", description = "ID of the workout to update", required = true, example = "101")
    })
    @PutMapping("/{userID}/workout/{workoutID}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long userID, @PathVariable Long workoutID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> existingWorkout = workoutService.findByWorkoutID(workoutID);
            if (existingWorkout.isPresent()) {
                Workout updatedWorkout = existingWorkout.get();
                if (workout.getExerciseType() != null) {
                    updatedWorkout.setExerciseType(workout.getExerciseType());
                }
                if (workout.getDuration() != 0) {
                    updatedWorkout.setDuration(workout.getDuration());
                }
                if (workout.getCalories() != 0) {
                    updatedWorkout.setCalories(workout.getCalories());
                }
                if (workout.getDate() != null) {
                    updatedWorkout.setDate(workout.getDate());
                }
                workoutService.createWorkout(updatedWorkout);

                //award any badges + update BadgeSocket
                badgeService.awardEligibleBadges(user.get());

                //update leaderboard
                LeaderBoardSocket.updateLeaderboard(user.get().getUserID());

                WorkoutIDResponse response = new WorkoutIDResponse("success", workoutID, "Workout updated");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    //Delete a workout
    // Delete a workout
    @Operation(
            summary = "Delete a workout",
            description = "Deletes a specific workout for a given user based on workout ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Workout deleted successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutIDResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or workout not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(name = "userID", description = "ID of the user", required = true, example = "1"),
            @Parameter(name = "workoutID", description = "ID of the workout to delete", required = true, example = "101")
    })
    @DeleteMapping("/{userID}/workout/{workoutID}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
            if (workout.isPresent()) {
                workoutService.deleteWorkout(workoutID);
                WorkoutIDResponse response = new WorkoutIDResponse("success", workoutID, "Workout deleted");
                return ResponseEntity.status(200).body(response);
            }
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }
}