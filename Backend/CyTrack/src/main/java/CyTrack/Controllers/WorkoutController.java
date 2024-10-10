package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/workouts/{userID}")
public class WorkoutController {
    private final WorkoutService workoutService;
    private final UserService userService;

    @Autowired
    public WorkoutController(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    @PostMapping("")
    public ResponseEntity<?> addWorkout(@PathVariable Long userID, @RequestBody Workout workout) {
        Optional<User> foundUser = userService.findByUserID(userID);
        if (foundUser.isPresent()) {
            workout.setUser(foundUser.get()); // Set the User object
            workout.setWorkoutID(null); // Ensure workoutID is not set manually
            Workout addedWorkout = workoutService.addWorkout(workout);
            WorkoutResponse response = new WorkoutResponse("success", addedWorkout.getWorkoutID(), "Workout added");
            return ResponseEntity.status(201).body(response);
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }
}