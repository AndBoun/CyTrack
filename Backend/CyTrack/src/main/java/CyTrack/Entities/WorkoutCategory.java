package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WorkoutCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutCategoryId;

    private String workoutCategoryName;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "workout_category_workout",
            joinColumns = @JoinColumn(name = "workout_category_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<Workout> workouts = new ArrayList<>();

    // =============================== Constructors ================================== //

    //default constructor
    public WorkoutCategory() {}

    public WorkoutCategory(String workoutCategoryName, User user) {
        this.workoutCategoryName = workoutCategoryName;
        this.user = user;
    }

    public WorkoutCategory(String workoutCategoryName) {
        this.workoutCategoryName = workoutCategoryName;
    }

    // =============================== Helper Methods ================================== //

    public void addWorkout(Workout workout) {
        if (!workouts.contains(workout)) {
            workouts.add(workout);
            workout.getWorkoutCategories().add(this); // Synchronize bidirectional relationship
        }
    }

    public void removeWorkout(Workout workout) {
        if (workouts.contains(workout)) {
            workouts.remove(workout);
            workout.getWorkoutCategories().remove(this); // Synchronize bidirectional relationship
        }
    }


    // =============================== Getters and Setters for each field ================================== //
    public Long getWorkoutCategoryId() {return workoutCategoryId; }

    public void setWorkoutCategoryId(Long workoutCategoryId) {this.workoutCategoryId = workoutCategoryId; }

    public String getWorkoutCategoryName() {return workoutCategoryName; }

    public void setWorkoutCategoryName(String workoutCategoryName) {this.workoutCategoryName = workoutCategoryName; }

    public User getUser() {return user; }

    public void setUser(User user) {this.user = user; }

    public List<Workout> getWorkouts() {return workouts; }

    public void setWorkouts(List<Workout> workouts) {this.workouts = workouts; }
}
