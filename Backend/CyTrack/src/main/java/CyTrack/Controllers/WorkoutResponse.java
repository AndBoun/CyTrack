package CyTrack.Controllers;


public class WorkoutResponse {
    private String status;
    private Data data;
    private String message;

    public WorkoutResponse(String status, Long workoutID, String message) {
        this.status = status;
        this.data = new Data(workoutID);
        this.message = message;
    }

    public WorkoutResponse(String status,Long workoutID, String exerciseType, int duration, int calories, String date, String message) {
        this.status = status;
        this.data = new Data(workoutID, exerciseType, duration, calories, date);
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Long workoutID;
        private String exerciseType;
        private int duration;
        private int calories;
        private String date;
        public Data(Long workoutID) {
            this.workoutID = workoutID;
        }
        public Data(Long workoutID, String exerciseType, int duration, int calories, String data ) {
            this.workoutID = workoutID;
            this.exerciseType = exerciseType;
            this.duration = duration;
            this.calories = calories;
            this.date = date;

        }


        public Long getWorkoutID() {
            return workoutID;
        }

        public void setWorkoutID(Long workoutID) {
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

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }



    }
}