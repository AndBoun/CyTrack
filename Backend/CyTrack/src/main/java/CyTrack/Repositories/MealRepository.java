package CyTrack.Repositories;

import CyTrack.Entities.Meal;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
//Repository for Meal entity
@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    //method for finding Meal by name. Could be useful later on.
    Optional<Meal> findByMealName(String mealName);

    //find meal by ID
    Optional<Meal> findByMealID(Long mealID);

    //list meals for given userID
    List<Meal> findByUser_UserID(Long userID);

    //list meals for given userID AND date
    List<Meal> findByUser_UserIDAndDate(Long userID, String date);

    @Transactional
    void deleteByMealID(Long mealId);
}
