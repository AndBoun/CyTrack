package CyTrack.Entities;

import CyTrack.Services.DeserializeDateService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "workouts", fetch = FetchType.LAZY)
    private List<WorkoutCategory> workoutCategories = new ArrayList<>();

    private String exerciseType;
    private int duration;
    private int calories;

    @JsonDeserialize(using = DeserializeDateService.class)
    private LocalDate date;

    private LocalDateTime startTime;
    private LocalDateTime endTime;


    // =============================== Constructors ================================== //
    public Workout() {}


    public Workout(String exerciseType, int duration, int calories, LocalDate date) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.calories = calories;
        this.date = date;
    }

    // Constructor for initializing workout without time
    public Workout(String exerciseType, int calories, LocalDate date) {
        this.exerciseType = exerciseType;
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

    public List<WorkoutCategory> getWorkoutCategories() {
        return workoutCategories;
    }

    public void setWorkoutCategories(List<WorkoutCategory> workoutCategories) {
        this.workoutCategories = workoutCategories;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Method to start workout
    public void startWorkout() {
        this.startTime = LocalDateTime.now();
    }

    // Method to end workout and calculate duration
    public void endWorkout() {
        this.endTime = LocalDateTime.now();
        this.duration = (int) Duration.between(startTime, endTime).toMinutes();
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


}
