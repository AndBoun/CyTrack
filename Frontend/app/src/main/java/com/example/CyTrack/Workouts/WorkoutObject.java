package com.example.CyTrack.Workouts;

/**
 * Represents a workout object with details about the exercise.
 */
public class WorkoutObject {
    /**
     * The type of exercise.
     */
    String exerciseType;

    /**
     * The duration of the workout.
     */
    String duration;

    /**
     * The calories burned during the workout.
     */
    String caloriesBurned;

    /**
     * The date of the workout.
     */
    String date;

    /**
     * The unique ID of the workout.
     */
    int workoutID;

    /**
     * Constructs a new WorkoutObject.
     *
     * @param exerciseType   the type of exercise
     * @param duration       the duration of the workout
     * @param caloriesBurned the calories burned during the workout
     * @param date           the date of the workout
     * @param workoutID      the unique ID of the workout
     */
    WorkoutObject(String exerciseType, String duration, String caloriesBurned, String date, int workoutID) {
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
        this.workoutID = workoutID;
    }

    /**
     * Gets the type of exercise.
     *
     * @return the type of exercise
     */
    public String getExerciseType() {
        return exerciseType;
    }

    /**
     * Sets the type of exercise.
     *
     * @param exerciseType the type of exercise
     */
    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    /**
     * Gets the duration of the workout.
     *
     * @return the duration of the workout
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the workout.
     *
     * @param duration the duration of the workout
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Gets the calories burned during the workout.
     *
     * @return the calories burned during the workout
     */
    public String getCaloriesBurned() {
        return caloriesBurned;
    }

    /**
     * Sets the calories burned during the workout.
     *
     * @param caloriesBurned the calories burned during the workout
     */
    public void setCaloriesBurned(String caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    /**
     * Gets the date of the workout.
     *
     * @return the date of the workout
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the workout.
     *
     * @param date the date of the workout
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets the unique ID of the workout.
     *
     * @return the unique ID of the workout
     */
    public int getWorkoutID() {
        return workoutID;
    }

    /**
     * Sets the unique ID of the workout.
     *
     * @param workoutID the unique ID of the workout
     */
    public void setWorkoutID(int workoutID) {
        this.workoutID = workoutID;
    }
}
