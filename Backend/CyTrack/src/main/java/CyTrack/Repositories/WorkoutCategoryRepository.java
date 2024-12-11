package CyTrack.Repositories;

import CyTrack.Entities.User;
import CyTrack.Entities.WorkoutCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutCategoryRepository extends JpaRepository<WorkoutCategory, Long> {
    Optional<WorkoutCategory> findByWorkoutCategoryName(String workoutCategory);
    Optional<WorkoutCategory> findByWorkoutCategoryId(Long workoutCategoryId);
    List<WorkoutCategory> findByUser(User user);
}
