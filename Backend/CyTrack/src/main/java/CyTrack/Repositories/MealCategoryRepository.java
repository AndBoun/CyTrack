package CyTrack.Repositories;

import CyTrack.Entities.MealCategory;
import CyTrack.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {
    Optional<MealCategory> findByMealCategoryName(String mealCategoryName);
    Optional<MealCategory> findByMealCategoryId(Long mealCategoryId);
    List<MealCategory> findByUser(User user);
}
