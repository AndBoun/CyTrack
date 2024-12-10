package CyTrack.Services;

import CyTrack.Entities.Meal;
import CyTrack.Entities.MealCategory;
import CyTrack.Entities.User;
import CyTrack.Repositories.MealCategoryRepository;
import CyTrack.Repositories.MealRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public MealService(MealRepository mealRepository, MealCategoryRepository mealCategoryRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.mealCategoryRepository = mealCategoryRepository;
        this.userRepository = userRepository;

    }

    public Meal createMeal(Meal meal) { return mealRepository.save(meal); }

    public Optional<Meal> findByMealID(Long mealID) { return mealRepository.findByMealID(mealID); }

    public void deleteMeal(Long mealID) { mealRepository.deleteByMealID(mealID); }

    public List<Meal> getAllMeals(Long userID) {return mealRepository.findByUser_UserID(userID); }

    public List<Meal> getMealsByUserIDAndDate(Long userID, String date) {
        return mealRepository.findByUser_UserIDAndDate(userID, date);
    }

    // Add a meal to a category
    public void addMealToCategory(Long mealId, Long categoryId) {
        MealCategory category = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Meal meal = mealRepository.findByMealID(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        category.getMeals().add(meal);
        meal.getMealCategories().add(category);

        mealCategoryRepository.save(category); // Save changes to the category
    }

    // Get all meals in a category
    public List<Meal> getMealsByCategory(Long categoryId) {
        MealCategory category = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return category.getMeals();
    }

    // Remove a meal from a category
    public void removeMealFromCategory(Long mealId, Long categoryId) {
        MealCategory category = mealCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Meal meal = mealRepository.findByMealID(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        category.getMeals().remove(meal);
        meal.getMealCategories().remove(category);

        mealCategoryRepository.save(category); // Save changes to the category
    }

    public List<Meal> getMealsByCategoryAndUser(Long mealCategoryId, Long userId) {
        MealCategory mealCategory = mealCategoryRepository.findById(mealCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("MealCategory with ID " + mealCategoryId + " does not exist."));

        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        // Filter meals by user and category
        return mealCategory.getMeals().stream()
                .filter(meal -> meal.getUser().getUserID().equals(userId))
                .toList();
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
