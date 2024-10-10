package CyTrack.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.WorkoutRepository;

@Service
public class WorkoutService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;

    }

    public Workout addWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }





}