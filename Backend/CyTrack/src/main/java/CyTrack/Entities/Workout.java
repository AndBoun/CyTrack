package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Workout {
    // =============================== Fields ================================== //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutID;
    // Many workouts can belong to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    private String exerciseType;
    private int duration;
    private int calories;
    private String date;

    // =============================== Constructors ================================== //
    public Workout() {}


    public Workout(String exerciseType, int duration, int calories, String date) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.calories = calories;
        this.date = date;
    }

    // =============================== Getters and Setters for each field ================================== //
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
