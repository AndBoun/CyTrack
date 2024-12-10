package CyTrack.Services;

import CyTrack.Entities.Workout;
import CyTrack.Entities.WorkoutCategory;
import CyTrack.Entities.User;
import CyTrack.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutCategoryService {
    private final UserRepository userRepository;
    private final WorkoutCategoryRepository workoutCategoryRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutCategoryService(UserRepository userRepository, WorkoutCategoryRepository workoutCategoryRepository, WorkoutRepository workoutRepository) {
        this.userRepository = userRepository;
        this.workoutCategoryRepository = workoutCategoryRepository;
        this.workoutRepository = workoutRepository;
    }

    /**
     * Fetch all workout categories by user ID.
     *
     * @param userID ID of the user
     * @return List of WorkoutCategory
     */
    public List<WorkoutCategory> getWorkoutCategoriesByUserId(Long userID) {
        return userRepository.findById(userID)
                .map(User::getWorkoutCategories)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userID + " does not exist."));
    }

    /**
     * Fetch a WorkoutCategory by its ID.
     *
     * @param workoutCategoryId ID of the WorkoutCategory
     * @return Optional of WorkoutCategory
     */
    public WorkoutCategory getWorkoutCategoryById(Long workoutCategoryId) {
        return workoutCategoryRepository.findById(workoutCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("WorkoutCategory with ID " + workoutCategoryId + " does not exist."));
    }


    public List<WorkoutCategory> getUserWorkoutCategories(User user) {
        return user.getWorkoutCategories();
    }

    public WorkoutCategory addWorkoutCategory(WorkoutCategory workoutCategory) {
        return workoutCategoryRepository.save(workoutCategory);
    }

    /**
     * Add a Workout to a WorkoutCategory.
     *
     * @param workoutCategoryId ID of the WorkoutCategory
     * @param workoutId         ID of the Workout to be added
     */
    public void addWorkoutToCategory(Long workoutCategoryId, Long workoutId) {
        WorkoutCategory workoutCategory = workoutCategoryRepository.findById(workoutCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("WorkoutCategory with ID " + workoutCategoryId + " does not exist."));

        Workout workout = workoutRepository.findByWorkoutID(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout with ID " + workoutId + " does not exist."));

        // Ensure the workout is not already assigned to this category
        if (workoutCategory.getWorkouts().contains(workout)) {
            throw new IllegalArgumentException("Workout is already in this category.");
        }

        // Add the workout to the category
        workoutCategory.getWorkouts().add(workout);

        // Persist the updates to both entities
        workoutCategoryRepository.save(workoutCategory);

        // If the Workout entity maintains the category relationship
        if (!workout.getWorkoutCategories().contains(workoutCategory)) {
            workout.getWorkoutCategories().add(workoutCategory);
            workoutRepository.save(workout);
        }
    }

    /**
     * Delete a workout category by user ID and category ID.
     *
     * @param userID        ID of the user
     * @param workoutCategoryId ID of the WorkoutCategory
     */
    public void deleteWorkoutCategory(Long userID, Long workoutCategoryId) {
        User user = userRepository.findByUserID(userID)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userID + " does not exist."));
        WorkoutCategory workoutCategory = workoutCategoryRepository.findById(workoutCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("WorkoutCategory with ID " + workoutCategoryId + " does not exist."));
        if (!workoutCategory.getUser().getUserID().equals(userID)) {
            throw new IllegalArgumentException("WorkoutCategory does not belong to the given user.");
        }
        workoutCategoryRepository.delete(workoutCategory);
    }

}
