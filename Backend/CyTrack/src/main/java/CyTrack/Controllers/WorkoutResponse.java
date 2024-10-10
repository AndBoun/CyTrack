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

    public WorkoutResponse(String status, Long workoutID, String workoutName, String workoutType, Long userID, String message) {
        this.status = status;
        this.data = new Data(workoutID, workoutName, workoutType, userID);
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
        private String workoutName;
        private String workoutType;
        private Long userID;

        public Data(Long workoutID) {
            this.workoutID = workoutID;
        }
        public Data(Long workoutID, String workoutName, String workoutType, Long userID) {
            this.workoutID = workoutID;
            this.workoutName = workoutName;
            this.workoutType = workoutType;
            this.userID = userID;
        }


        public Long getWorkoutID() {
            return workoutID;
        }

        public void setWorkoutID(Long workoutID) {
            this.workoutID = workoutID;
        }

        public String getWorkoutName() {
            return workoutName;
        }

        public void setWorkoutName(String workoutName) {
            this.workoutName = workoutName;
        }

        public String getWorkoutType() {
            return workoutType;
        }

        public void setWorkoutType(String workoutType) {
            this.workoutType = workoutType;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }
    }
}