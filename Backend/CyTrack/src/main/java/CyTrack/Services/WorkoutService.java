package CyTrack.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.WorkoutRepository;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Workout addWorkout(Workout workout) {
        if (workout.getExerciseType() == null || workout.getUser() == null) {
            throw new IllegalArgumentException("Exercise type and user cannot be null");
        }
        try {
            return workoutRepository.save(workout);
        } catch (Exception e) {
            throw e;
        }
    }
}