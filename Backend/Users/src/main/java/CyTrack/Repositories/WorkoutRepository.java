package CyTrack.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import CyTrack.Entities.Workout;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findByWorkoutID(Long workoutID);
    Optional<Workout> findByWorkoutName(String workoutName);
    Optional<Workout> findByWorkoutType(String exerciseType);
    Optional<Workout> findByUserID(Long userID);

}
