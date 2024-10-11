package CyTrack.Repositories;

import CyTrack.Entities.Workout;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);
    Optional<Workout> findByWorkoutID(Long workoutID);
    @Transactional
    void deleteByWorkoutID(Long workoutID);



}