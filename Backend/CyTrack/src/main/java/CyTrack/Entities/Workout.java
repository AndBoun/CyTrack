package CyTrack.Entities;

import jakarta.persistence.*;

@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workoutID;
    private Long userID;
    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private String time;

    public Workout() {}

    public Workout(Long userID, String exerciseType, int duration, int caloriesBurned, String time) {
        this.userID = userID;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.time = time;
    }

    public Long getWorkoutID() {
        return workoutID;
    }

    public void setWorkoutID(Long workoutID) {
        this.workoutID = workoutID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
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