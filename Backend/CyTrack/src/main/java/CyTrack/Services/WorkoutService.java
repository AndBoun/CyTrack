package CyTrack.Services;

import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Entities.Workout;
import CyTrack.Repositories.WorkoutRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
//Service for Workout entity
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final BadgeService badgeService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository,
                          BadgeService badgeService) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.badgeService = badgeService;
    }

    //Create workout
    public Workout createWorkout(Workout workout) {
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
    public List<Workout> getWorkoutsByUserIDAndDate(Long userID, String date) {
        return workoutRepository.findByUser_UserIDAndDate(userID, date);
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
    public int getCaloriesByDate(Long userID, String date) {
        List<Workout> workouts = getWorkoutsByUserIDAndDate(userID, date);
        return workouts.stream().mapToInt(Workout::getCalories).sum();
    }

    public int getWorkoutTimeByDate(Long userID, String date) {
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
            badgeService.awardTimeBadge(user);

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
            badgeService.awardTimeBadge(user);

            return updatedTotalTime; // Return updated total time for verification/testing purposes
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

}