package CyTrack.Controllers;


public class WorkoutIDResponse {
    private String status;
    private Data data;
    private String message;

    public WorkoutIDResponse(String status, Long workoutID, String message) {
        this.status = status; // Set the status
        this.data = new Data(workoutID); // Set the data with the workout ID
        this.message = message; // Set the message
    }

    public String getStatus() { // Getter for status
        return status;
    }

    public void setStatus(String status) { // Setter for status
        this.status = status;
    }

    public Data getData() { // Getter for data
        return data;
    }

    public void setData(Data data) { // Setter for data
        this.data = data;
    }


    public String getMessage() { // Getter for message
        return message;
    }

    public void setMessage(String message) { // Setter for message
        this.message = message;
    }

    public static class Data {
        private Long workoutID; // Workout ID

        public Data(Long workoutID) {
            this.workoutID = workoutID; // Set the workout ID
        }

        public Long getWorkoutID() { // Getter for workout ID
            return workoutID;
        }

        public void setWorkoutID(Long workoutID) { // Setter for workout ID
            this.workoutID = workoutID;
        }
    }
}