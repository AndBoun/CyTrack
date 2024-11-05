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

    public Optional<Meal> findByMealID(Long mealID) { return mealRepository.findByMealID(mealID); }

    public void deleteMeal(Long mealID) { mealRepository.deleteByMealID(mealID); }

    public List<Meal> getAllMeals(Long userID) {return mealRepository.findByUser_UserID(userID); }

    public List<Meal> getMealsByUserIDAndDate(Long userID, String date) {
        return mealRepository.findByUser_UserIDAndDate(userID, date);
    }

    //Calculate total calories, protein, and carbs for a given date
    public NutrientSummary calculateTotalNutrients(Long userID, String date) {
        List<Meal> meals = getMealsByUserIDAndDate(userID, date);
        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;

        for (Meal meal : meals) {
            totalCalories += meal.getCalories() != null ? meal.getCalories() : 0;
            totalProtein += meal.getProtein() != null ? meal.getProtein() : 0;
            totalCarbs += meal.getCarbs() != null ? meal.getCarbs() : 0;
        }

        return new NutrientSummary(totalCalories, totalProtein, totalCarbs);
    }

    // Inner class to hold nutrient summary
    public static class NutrientSummary {
        private int totalCalories;
        private int totalProtein;
        private int totalCarbs;

        public NutrientSummary(int totalCalories, int totalProtein, int totalCarbs) {
            this.totalCalories = totalCalories;
            this.totalProtein = totalProtein;
            this.totalCarbs = totalCarbs;
        }

        public int getTotalCalories() {
            return totalCalories;
        }

        public int getTotalProtein() {
            return totalProtein;
        }

        public int getTotalCarbs() {
            return totalCarbs;
        }
    }
}
