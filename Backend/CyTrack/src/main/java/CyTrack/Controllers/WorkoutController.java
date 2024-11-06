package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Services.BadgeService;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
//Controller for Workout entity
@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;
    private final BadgeService badgeService;

    //Constructor
    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService,
                             BadgeService badgeService) {
        this.workoutService = workoutService;
        this.userService = userService;
        this.badgeService = badgeService;
    }

    //Create a workout
    @PostMapping("/{userID}/workout")
    public ResponseEntity<?> createWorkout(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            workout.setUser(user.get());
            Workout newWorkout = workoutService.createWorkout(workout);

            badgeService.awardEligibleBadges(user.get());

            WorkoutIDResponse response = new WorkoutIDResponse("success", newWorkout.getWorkoutID(), "Workout created");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    // Start a workout
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
    @PostMapping("/{userID}/workout/{workoutID}/end")
    public ResponseEntity<?> endWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
            if (workout.isPresent()) {
                Workout endedWorkout = workoutService.endWorkout(workoutID);
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
    @GetMapping("/{userID}/workoutByDate")
    public ResponseEntity<?> getWorkoutsByDate(@PathVariable Long userID,
                                               @RequestBody WorkoutRequest workoutRequest) {
        String date = workoutRequest.getDate();
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Workout> workouts = workoutService.getWorkoutsByUserIDAndDate(userID, date);
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
    @GetMapping("/{userID}/totalCalories/{date}")
    public ResponseEntity<?> getTotalCaloriesByDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            int totalCalories = workoutService.getCaloriesByDate(userID, date);
            int totalWorkoutTime = workoutService.getWorkoutTimeByDate(userID, date); // Assuming you want to include workout time too
            WorkoutSummaryResponse response = new WorkoutSummaryResponse("success", totalCalories, totalWorkoutTime, date, "Total calories burned for " + date);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "User not found"));
        }
    }

    //Get total workout time for a given date
    @GetMapping("/{userID}/totalWorkoutTime/{date}")
    public ResponseEntity<?> getTotalWorkoutTimeByDate(@PathVariable Long userID, @PathVariable String date) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            int totalWorkoutTime = workoutService.getWorkoutTimeByDate(userID, date);
            WorkoutSummaryResponse response = new WorkoutSummaryResponse("success", 0, totalWorkoutTime, date, "Total workout time for " + date); // totalCalories set to 0
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("error", 404, "User not found", "User not found"));
        }
    }

    //Update a workout
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

                badgeService.awardEligibleBadges(user.get());

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