package CyTrack.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.WorkoutRepository;

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

    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public Optional<Workout> findByWorkoutID(Long workoutID) {
        return workoutRepository.findByWorkoutID(workoutID);
    }

    public void deleteWorkout(Long workoutID) {
        workoutRepository.deleteById(workoutID);
    }

    public List<Workout> getAllWorkouts(Long userID) {
        return workoutRepository.findByUser_UserID(userID);
    }

}