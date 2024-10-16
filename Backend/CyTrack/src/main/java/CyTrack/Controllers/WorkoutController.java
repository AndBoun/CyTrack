package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.UserRepository;
import CyTrack.Repositories.WorkoutRepository;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;


    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @PostMapping("/{userID}/workout")
    public ResponseEntity<?> createWorkout(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            workout.setUser(user.get()); // Set the user property
            Workout newWorkout = workoutService.createWorkout(workout);
            WorkoutResponse response = new WorkoutResponse("success", newWorkout.getWorkoutID(), "Workout created");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    @GetMapping("/{userID}/workout/{workoutID}")
    public ResponseEntity<?> getWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
        Optional<Workout> workout = workoutService.findByWorkoutID(workoutID);
        if (workout.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        Workout foundWorkout = workout.get();
        WorkoutResponse response = new WorkoutResponse(
                "success",
                foundWorkout.getWorkoutID(),
                foundWorkout.getExerciseType(),
                foundWorkout.getDuration(),
                foundWorkout.getCaloriesBurned(),
                foundWorkout.getDate(),
                "Workout found");
        return ResponseEntity.status(200).body(response);
    }
}