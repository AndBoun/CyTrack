package CyTrack.Controllers;

public class MealResponse {
    private String status;
    private String message;
    private Data data;

    public MealResponse(String status, String message, Long mealID) {
        this.status = status;
        this.message = message;
        this.data  = new Data(mealID);
    }

    public MealResponse(String status, String message, Long mealID, String mealName, Integer calories, Integer protein, Integer carbs, String time) {
        this.status = status;
        this.message = message;
        this.data = new Data(mealID, mealName, calories, protein, carbs, time);
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
        private Long mealID;
        private String mealName;
        private Integer calories;
        private Integer protein;
        private Integer carbs;
        private String time;

        public Data(Long mealID) {this.mealID = mealID;}

        public Data (Long mealID, String mealName, Integer calories, Integer protein, Integer carbs, String time) {
            this.mealID = mealID;
            this.mealName = mealName;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.time = time;
        }

        public Long getMealID() {
            return mealID;
        }

        public void setMealID(Long mealID) {
            this.mealID = mealID;
        }

        public String getMealName() {
            return mealName;
        }

        public void setMealName(String mealName) {
            this.mealName = mealName;
        }

        public Integer getCalories() {
            return calories;
        }

        public void setCalories(Integer calories) {
            this.calories = calories;
        }

        public Integer getProtein() {
            return protein;
        }

        public void setProtein(Integer protein) {
            this.protein = protein;
        }

        public Integer getCarbs() {
            return carbs;
        }

        public void setCarbs(Integer carbs) {
            this.carbs = carbs;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
