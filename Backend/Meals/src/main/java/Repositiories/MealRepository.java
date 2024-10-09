package Repositiories;

import Entity.Meal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Integer> {

    //method for finding Meal by name. Could be useful later on.
    Optional<Meal> findByName(String mealName);
}
