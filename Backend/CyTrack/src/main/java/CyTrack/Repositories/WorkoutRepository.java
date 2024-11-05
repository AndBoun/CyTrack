package CyTrack.Repositories;

import CyTrack.Entities.Workout;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//Repository for Workout entity
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findByWorkoutID(Long workoutID);
    List<Workout> findByUser_UserID(Long userID);
    List<Workout> findByUser_UserIDAndDate(Long userID, String date);

    @Transactional
    void deleteByWorkoutID(Long workoutID);
}