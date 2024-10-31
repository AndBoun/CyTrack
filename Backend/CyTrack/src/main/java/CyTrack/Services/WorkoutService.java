package CyTrack.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.WorkoutRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//Service for Workout entity
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    //Create workout
    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    //find workout by ID
    public Optional<Workout> findByWorkoutID(Long workoutID) {
        return workoutRepository.findByWorkoutID(workoutID);
    }

    //delete workout by ID
    public void deleteWorkout(Long workoutID) {
        workoutRepository.deleteById(workoutID);
    }

    //get all workouts by given userID
    public List<Workout> getAllWorkouts(Long userID) {
        return workoutRepository.findByUser_UserID(userID);
    }

    // Start a workout by setting the start time
    public Workout startWorkout(Long workoutID) {
        Optional<Workout> workoutOptional = workoutRepository.findByWorkoutID(workoutID);
        if (workoutOptional.isPresent()) {
            Workout workout = workoutOptional.get();
            workout.setStartTime(LocalDateTime.now());
            return workoutRepository.save(workout);
        } else {
            throw new IllegalArgumentException("Workout not found");
        }
    }

    // End a workout by setting the end time and calculating duration
    @Transactional
    public Workout endWorkout(Long workoutID) {
        Optional<Workout> workoutOptional = workoutRepository.findByWorkoutID(workoutID);
        if (workoutOptional.isPresent()) {
            Workout workout = workoutOptional.get();
            workout.endWorkout(); // Call the endWorkout method on the entity to set end time and calculate duration
            return workoutRepository.save(workout); // Persist changes to the database
        } else {
            throw new IllegalArgumentException("Workout not found");
        }
    }

}