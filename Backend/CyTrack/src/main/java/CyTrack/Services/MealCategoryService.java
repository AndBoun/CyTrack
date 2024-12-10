package CyTrack.Services;

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
public class MealCategoryService {

    private final UserRepository userRepository;
    private final MealCategoryRepository mealCategoryRepository;

    @Autowired
    public MealCategoryService(UserRepository userRepository, MealCategoryRepository mealCategoryRepository) {
        this.userRepository = userRepository;
        this.mealCategoryRepository = mealCategoryRepository;
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

    public List<MealCategory> getUserMealCategories(User user) {
        return user.getMealCategories();
    }

    public MealCategory addMealCategory(MealCategory mealCategory) {
        return mealCategoryRepository.save(mealCategory);
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
