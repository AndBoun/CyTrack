package CyTrack.Controllers;

public class WorkoutIDResponse {
    private String status;
    private Data data;
    private String message;

    public WorkoutIDResponse(String status, Long workoutID, String message) {
        this.status = status;
        this.data = new Data(workoutID);
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data{
        private Long workoutID;

        public Data(Long workoutID) {
            this.workoutID = workoutID;
        }

        public Long getWorkoutID() {
            return workoutID;
        }

        public void setWorkoutID(Long workoutID) {
            this.workoutID = workoutID;
        }
    }
}