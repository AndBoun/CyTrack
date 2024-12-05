package CyTrack.Controllers;

import java.time.LocalDate;
import java.util.List;
//Response for Workout
public class WorkoutResponse {
    private String status;
    private Data data;
    private String message;

    public WorkoutResponse(String status, List<WorkoutData> workouts, String message) {
        this.status = status;
        this.data = new Data(workouts);
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {return data; }

    public void setData(Data data) {
        this.data = data;
    }


    public static class Data {
        private List<WorkoutData> workouts;

        //default/no-arg constructor
        public Data() {

        }


        public Data(List<WorkoutData> workouts) {
            this.workouts = workouts;
        }

        public List<WorkoutData> getWorkouts() {
            return workouts;
        }

        public void setWorkouts(List<WorkoutData> workouts) {
            this.workouts = workouts;
        }
    }

    public static class WorkoutData {
        private String exerciseType;
        private int duration;
        private int calories;
        private LocalDate date;
        private Long workoutID;

        //default/no-arg constructor
        public WorkoutData() {
        }

        public WorkoutData(String exerciseType, int duration, int calories, LocalDate date, Long workoutID) {
            this.exerciseType = exerciseType;
            this.duration = duration;
            this.calories = calories;
            this.date = date;
            this.workoutID = workoutID;
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

        public Long getWorkoutID() {
            return workoutID;
        }

        public void setWorkoutID(Long workoutID) {
            this.workoutID = workoutID;
        }
    }
}