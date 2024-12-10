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

@Service
public class MealCategoryService {

    private final UserRepository userRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final MealRepository mealRepository;

    @Autowired
    public MealCategoryService(UserRepository userRepository, MealCategoryRepository mealCategoryRepository, MealRepository mealRepository) {
        this.userRepository = userRepository;
        this.mealCategoryRepository = mealCategoryRepository;
        this.mealRepository = mealRepository;
    }

    /**
     * Fetch all meal categories by user ID.
     *
     * @param userID ID of the user
     * @return List of MealCategory
     */
    public List<MealCategory> getMealCategoriesByUserId(Long userID) {
        return userRepository.findById(userID)
                .map(User::getMealCategories)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userID + " does not exist."));
    }

    /**
     * Fetch a MealCategory by its ID.
     *
     * @param mealCategoryId ID of the MealCategory
     * @return Optional of MealCategory
     */
    public MealCategory getMealCategoryById(Long mealCategoryId) {
        return mealCategoryRepository.findById(mealCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("MealCategory with ID " + mealCategoryId + " does not exist."));
    }


    public List<MealCategory> getUserMealCategories(User user) {
        return user.getMealCategories();
    }

    public MealCategory addMealCategory(MealCategory mealCategory) {
        return mealCategoryRepository.save(mealCategory);
    }

    /**
     * Add a Meal to a MealCategory.
     *
     * @param mealCategoryId ID of the MealCategory
     * @param mealId         ID of the Meal to be added
     */
    public void addMealToCategory(Long mealCategoryId, Long mealId) {
        MealCategory mealCategory = mealCategoryRepository.findById(mealCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("MealCategory with ID " + mealCategoryId + " does not exist."));

        Meal meal = mealRepository.findByMealID(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal with ID " + mealId + " does not exist."));

        // Ensure the meal is not already assigned to this category
        if (mealCategory.getMeals().contains(meal)) {
            throw new IllegalArgumentException("Meal is already in this category.");
        }

        // Add the meal to the category
        mealCategory.getMeals().add(meal);

        // Persist the updates to both entities
        mealCategoryRepository.save(mealCategory);

        // If the Meal entity maintains the category relationship
        if (!meal.getMealCategories().contains(mealCategory)) {
            meal.getMealCategories().add(mealCategory);
            mealRepository.save(meal);
        }
    }

    /**
     * Delete a meal category by user ID and category ID.
     *
     * @param userID        ID of the user
     * @param mealCategoryId ID of the MealCategory
     */
    public void deleteMealCategory(Long userID, Long mealCategoryId) {
        User user = userRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userID + " does not exist."));
        MealCategory mealCategory = mealCategoryRepository.findById(mealCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("MealCategory with ID " + mealCategoryId + " does not exist."));
        if (!mealCategory.getUser().getUserID().equals(userID)) {
            throw new IllegalArgumentException("MealCategory does not belong to the given user.");
        }
        mealCategoryRepository.delete(mealCategory);
    }

}
