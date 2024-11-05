package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
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

    //Constructor
    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }
    //Create a workout
    @PostMapping("/{userID}/workout")
    public ResponseEntity<?> createWorkout(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            workout.setUser(user.get());
            Workout newWorkout = workoutService.createWorkout(workout);

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

    //Get all workouts by user ID
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

    @GetMapping("/{userID}/workoutByDate")
    public ResponseEntity<?> getWorkoutsByDate(@PathVariable Long userID,
                                               @RequestBody WorkoutRequest workoutRequest) {
        String date = workoutRequest.getDate();
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            List<Workout> workouts = workoutService.getWorkoutsByDate(userID, date);
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