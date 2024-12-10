package CyTrack.Responses;

import java.util.List;

public class WorkoutCategoryResponse {
    private String status;
    private Data data;
    private String message;

    public WorkoutCategoryResponse(String status, List<WorkoutCategoryData> workoutCategories, String message) {
        this.status = status;
        this.data = new Data(workoutCategories);
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

    //inner class to store our WorkoutCategoryData objects
    public static class Data {
        private List<WorkoutCategoryData> workoutCategories;

        public Data(List<WorkoutCategoryData> workoutCategories) {
            this.workoutCategories = workoutCategories;
        }

        public List<WorkoutCategoryData> getWorkoutCategories() {
            return workoutCategories;
        }

        public void setWorkoutCategories(List<WorkoutCategoryData> workoutCategories) {
            this.workoutCategories = workoutCategories;
        }
    }

    //inner class to store data about WorkoutCategory
    public static class WorkoutCategoryData {
        private Long workoutCategoryID;
        private String workoutCategoryName;

        public WorkoutCategoryData(Long workoutCategoryID, String workoutCategoryName) {
            this.workoutCategoryID = workoutCategoryID;
            this.workoutCategoryName = workoutCategoryName;
        }

        public Long getWorkoutCategoryID() { return workoutCategoryID; }

        public void setWorkoutCategoryID(Long workoutCategoryID) { this.workoutCategoryID = workoutCategoryID; }

        public String getWorkoutCategoryName() { return workoutCategoryName; }

        public void setWorkoutCategoryName(String workoutCategoryName) { this.workoutCategoryName = workoutCategoryName; }
    }
}
