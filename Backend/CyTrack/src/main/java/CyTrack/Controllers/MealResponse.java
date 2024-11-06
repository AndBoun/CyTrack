package CyTrack.Controllers;

import java.util.List;

public class MealResponse {
    private String status;
    private Data data;
    private String message;

    public MealResponse(String status, List<MealData> meals, String message) {
        this.status = status;
        this.data = new Data(meals);
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
        private List<MealData> meals;

        public Data(List<MealData> meals) { this.meals = meals; }

        public List<MealData> getMeals() { return meals; };

        public void setMeals(List<MealData> meals) { this.meals = meals; }
    }

    public static class MealData {
        private Long mealID;
        private String mealName;
        private Integer calories;
        private Integer protein;
        private Integer carbs;
        private String time;
        private String date;


        public MealData (Long mealID, String mealName, Integer calories,
                     Integer protein, Integer carbs, String time, String date) {
            this.mealID = mealID;
            this.mealName = mealName;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.time = time;
            this.date = date;
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

        public String getDate() {return date; }

        public void setDate(String date) {this.date = date; }
    }

}
