package CyTrack.Entities;

import jakarta.persistence.*;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremented primary key
    private Long workoutID;

    @ManyToOne
    @JoinColumn(name = "userID")  // Foreign key to User
    private User user;

    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private String time;

    // Default constructor
    public Workout() {}

    // Constructor with fields
    public Workout(String exerciseType, int duration, int caloriesBurned, String time) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.time = time;
    }

    // Getters and setters
    public Long getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(Long workoutID) {
        this.workoutID = workoutID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
