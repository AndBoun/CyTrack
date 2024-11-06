package CyTrack.Controllers;

// Response for workout summary
public class WorkoutSummaryResponse {
    private String status;
    private Data data; // Changed to encapsulate total calories, total workout time, and date
    private String message;

    public WorkoutSummaryResponse(String status, int totalCalories, int totalWorkoutTime, String date, String message) {
        this.status = status;
        this.data = new Data(totalCalories, totalWorkoutTime, date); // Initialize the Data object
        this.message = message;
    }

    public String getStatus() {return status; }

    public void setStatus(String status) {this.status = status; }

    public Data getData() {return data; }

    public void setData(Data data) {this.data = data; }

    public String getMessage() {return message; }

    public void setMessage(String message) {this.message = message; }

    // Inner class to encapsulate data
    public static class Data {
        private int totalCalories;
        private int totalWorkoutTime;
        private String date;

        public Data(int totalCalories, int totalWorkoutTime, String date) {
            this.totalCalories = totalCalories;
            this.totalWorkoutTime = totalWorkoutTime;
            this.date = date;
        }

        public int getTotalCalories() {return totalCalories; }

        public void setTotalCalories(int totalCalories) {this.totalCalories = totalCalories; }

        public int getTotalWorkoutTime() {return totalWorkoutTime; }

        public void setTotalWorkoutTime(int totalWorkoutTime) {this.totalWorkoutTime = totalWorkoutTime; }

        public String getDate() {return date; }

        public void setDate(String date) {this.date = date; }
    }
}
