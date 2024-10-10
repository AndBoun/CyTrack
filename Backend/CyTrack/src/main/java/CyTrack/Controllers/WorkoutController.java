package CyTrack.Controllers;

import CyTrack.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import CyTrack.Entities.Workout;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;

@RestController
@RequestMapping("/workout/{userID}")
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
        if (foundUser.isPresent()){
            workout.setUserID(userID);
            Workout addedWorkout = workoutService.addWorkout(workout);
            WorkoutResponse response = new WorkoutResponse("success", addedWorkout.getWorkoutID(), "Workout added");
            return ResponseEntity.status(201).body(response);
        }
        else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

}