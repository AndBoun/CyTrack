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

import java.util.Optional;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {
   @Autowired
    WorkoutRepository workoutRepository;

   @Autowired
    UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<?> createWorkout(@RequestBody Workout workout) {
        if (workout == null) {
            return ResponseEntity.badRequest().body(null);
        }
        workoutRepository.save(workout);
        WorkoutResponse response = new WorkoutResponse("success", workout.getWorkoutID(), "Workout created");
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{userID}/{workoutID}")
    public ResponseEntity<?> getWorkout(@PathVariable Long userID, @PathVariable Long workoutID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
        Optional<Workout> workout = workoutRepository.findByWorkoutID(workoutID);
        if (workout.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "Workout not found", "Workout not found");
            return ResponseEntity.status(404).body(response);
        }
        WorkoutResponse response = new WorkoutResponse("success", workout.get().getWorkoutID(), "Workout found");
        return ResponseEntity.status(200).body(response);
    }
}