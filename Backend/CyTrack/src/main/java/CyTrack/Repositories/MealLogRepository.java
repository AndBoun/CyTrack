package CyTrack.Repositories;

import CyTrack.Entities.MealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {

    Optional<MealLog> findByLogDate(String logDate);
}
