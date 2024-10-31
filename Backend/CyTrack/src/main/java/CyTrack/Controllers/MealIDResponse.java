package CyTrack.Controllers;

public class MealIDResponse {
    private String status;
    private Data data;
    private String message;

    public MealIDResponse(String status, Long mealID, String message) {
        this.status = status;
        this.data = new Data(mealID);
        this.message = message;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {return data; }

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
        private Long mealID;

        public Data(Long mealID) { this.mealID = mealID; }

        public Long getMealID() { return mealID; }

        public void setMealID(Long mealID) { this.mealID = mealID; }
    }
}
