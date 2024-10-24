package CyTrack.Services;

import CyTrack.Entities.Meal;
import CyTrack.Repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;

    @Autowired
    public MealService(MealRepository mealRepository) {this.mealRepository = mealRepository; }

    public Meal createMeal(Meal meal) { return mealRepository.save(meal); }

    public Optional<Meal> findByMealID(Long mealID) { return mealRepository.findByMealId(mealID); }

    public void deleteMeal(Long mealID) { mealRepository.deleteByMealId(mealID); }

    public List<Meal> getAllMeals(Long userID) {return mealRepository.findyByUser_UserID(userID); }
}
