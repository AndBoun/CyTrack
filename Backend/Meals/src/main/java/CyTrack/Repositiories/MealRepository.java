package CyTrack.Repositiories;

import CyTrack.Entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    //method for finding Meal by name. Could be useful later on.
    Optional<Meal> findByMealName(String mealName);

    Optional<Meal> findByMealId(Integer mealId);


}
