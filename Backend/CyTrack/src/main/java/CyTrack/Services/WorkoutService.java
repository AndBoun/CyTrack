package CyTrack.Services;

import CyTrack.Entities.*;
import CyTrack.Repositories.UserRepository;
import CyTrack.Repositories.WorkoutCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Repositories.WorkoutRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Service for Workout entity
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutCategoryRepository workoutCategoryRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, WorkoutCategoryRepository workoutCategoryRepository, UserRepository userRepository,
                          BadgeService badgeService) {
        this.workoutRepository = workoutRepository;
        this.workoutCategoryRepository = workoutCategoryRepository;
        this.userRepository = userRepository;
        this.badgeService = badgeService;
    }

    //Create workout
    public Workout createWorkout(Workout workout) {
        User userToAddWorkout = workout.getUser();
        userToAddWorkout.setTotalTime(userToAddWorkout.getTotalTime() + workout.getDuration());

        //updateStreak(userToAddWorkout);

        return workoutRepository.save(workout);

    }

    //find workout by ID
    public Optional<Workout> findByWorkoutID(Long workoutID) {
        return workoutRepository.findByWorkoutID(workoutID);
    }

    //delete workout by ID
    public void deleteWorkout(Long workoutID) {
        workoutRepository.deleteById(workoutID);
    }

    //get all workouts by given userID
    public List<Workout> getAllWorkouts(Long userID) {
        return workoutRepository.findByUser_UserID(userID);
    }

    //get all workouts for a given userID AND date
    public List<Workout> getWorkoutsByUserIDAndDate(Long userID, LocalDate date) {
        return workoutRepository.findByUser_UserIDAndDate(userID, date);
    }

    // Add a workout to a category
    public void addWorkoutToCategory(Long workoutId, Long categoryId) {
        WorkoutCategory category = workoutCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Workout workout = workoutRepository.findByWorkoutID(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        category.getWorkouts().add(workout);
        workout.getWorkoutCategories().add(category);

        workoutCategoryRepository.save(category); // Save changes to the category
    }

    // Get all workouts in a category
    public List<Workout> getWorkoutsByCategory(Long categoryId) {
        WorkoutCategory category = workoutCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return category.getWorkouts();
    }

    // Remove a workout from a category
    public void removeWorkoutFromCategory(Long workoutId, Long categoryId) {
        WorkoutCategory category = workoutCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        Workout workout = workoutRepository.findByWorkoutID(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("Workout not found"));

        category.getWorkouts().remove(workout);
        workout.getWorkoutCategories().remove(category);

        workoutCategoryRepository.save(category); // Save changes to the category
    }

    public List<Workout> getWorkoutsByCategoryAndUser(Long workoutCategoryId, Long userId) {
        WorkoutCategory workoutCategory = workoutCategoryRepository.findById(workoutCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("WorkoutCategory with ID " + workoutCategoryId + " does not exist."));

        User user = userRepository.findByUserID(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " does not exist."));

        // Filter workouts by user and category
        return workoutCategory.getWorkouts().stream()
                .filter(workout -> workout.getUser().getUserID().equals(userId))
                .toList();
    }

    // Start a workout by setting the start time
    public Workout startWorkout(Long workoutID) {
        Optional<Workout> workoutOptional = workoutRepository.findByWorkoutID(workoutID);
        if (workoutOptional.isPresent()) {
            Workout workout = workoutOptional.get();
            workout.setStartTime(LocalDateTime.now());
            return workoutRepository.save(workout);
        } else {
            throw new IllegalArgumentException("Workout not found");
        }
    }

    //
    public int getCaloriesByDate(Long userID, LocalDate date) {
        List<Workout> workouts = getWorkoutsByUserIDAndDate(userID, date);
        return workouts.stream().mapToInt(Workout::getCalories).sum();
    }

    public int getWorkoutTimeByDate(Long userID, LocalDate date) {
        List<Workout> workouts = getWorkoutsByUserIDAndDate(userID, date);
        return workouts.stream().mapToInt(Workout::getDuration).sum();
    }


    @Transactional
    public Workout endWorkout(Long workoutID) {
        Optional<Workout> workoutOptional = workoutRepository.findByWorkoutID(workoutID);
        if (workoutOptional.isPresent()) {
            Workout workout = workoutOptional.get();
            workout.endWorkout(); // Sets end time and calculates duration

            // Fetch the associated user and update total time
            User user = workout.getUser();
            int workoutDuration = workout.getDuration(); // Assuming duration is calculated in minutes
            user.setTotalTime(user.getTotalTime() + workoutDuration);

            userRepository.save(user); // Save user with updated total time

            // Attempt to award the LifetimeTimeBadge based on the updated total time
            badgeService.awardEligibleBadges(user);

            return workoutRepository.save(workout); // Save workout with end time and duration
        } else {
            throw new IllegalArgumentException("Workout not found");
        }
    }



    /**
     * Manually update total time for a user and attempt to award a badge.
     *
     * ===USED FOR TESTING AWARDING TIME BADGE WITHOUT NEEDING TO WAIT TO END A WORKOUT===
     *
     * @param userID    the ID of the user to update
     * @param timeToAdd the amount of time to add to the user's total time (in minutes)
     * @return the updated total time for the user
     */
    @Transactional
    public int updateUserTotalTimeAndAwardBadge(Long userID, int timeToAdd) {
        Optional<User> userOptional = userRepository.findById(userID);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update total time
            int updatedTotalTime = user.getTotalTime() + timeToAdd;
            user.setTotalTime(updatedTotalTime);

            // Save user with updated total time
            userRepository.save(user);

            // Attempt to award the LifetimeTimeBadge based on updated total time
            badgeService.awardEligibleBadges(user);

            return updatedTotalTime; // Return updated total time for verification/testing purposes
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}