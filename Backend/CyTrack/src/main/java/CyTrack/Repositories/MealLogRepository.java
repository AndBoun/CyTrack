package CyTrack.Repositories;

import CyTrack.Entities.MealLog;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealLogRepository {

    Optional<MealLog> findByLogDate(String logDate);
}
