package CyTrack.Repositories;

import CyTrack.Entities.Meal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    //method for finding Meal by name. Could be useful later on.
    Optional<Meal> findByMealName(String mealName);
    List<Meal> findyByUser_UserID(Long userID);
    Optional<Meal> findByMealId(Long mealID);

    @Transactional
    void deleteByMealId(Long mealId);
}
