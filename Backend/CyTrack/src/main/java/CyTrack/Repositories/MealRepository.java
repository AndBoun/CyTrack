package CyTrack.Repositories;

import CyTrack.Entities.Meal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//Repository for Meal entity
@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    //method for finding Meal by name. Could be useful later on.
    Optional<Meal> findByMealName(String mealName);

    Optional<Meal> findByMealId(Long mealId);

    @Transactional
    void deleteByMealId(Long mealId);
}
