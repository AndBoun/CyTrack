package CyTrack.Responses;

import java.util.List;

public class MealCategoryResponse {
    private String status;
    private Data data;
    private String message;

    public MealCategoryResponse(String status, List<MealCategoryData> mealCategories, String message) {
        this.status = status;
        this.data = new Data(mealCategories);
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

    //inner class to store our MealCategoryData objects
    public static class Data {
        private List<MealCategoryData> mealCategories;

        public Data(List<MealCategoryData> mealCategories) {
            this.mealCategories = mealCategories;
        }

        public List<MealCategoryData> getMealCategories() {
            return mealCategories;
        }

        public void setMealCategories(List<MealCategoryData> mealCategories) {
            this.mealCategories = mealCategories;
        }
    }

    //inner class to store data about MealCategory
    public static class MealCategoryData {
        private Long mealCategoryID;
        private String mealCategoryName;

        public MealCategoryData(Long mealCategoryID, String mealCategoryName) {
            this.mealCategoryID = mealCategoryID;
            this.mealCategoryName = mealCategoryName;
        }

        public Long getMealCategoryID() { return mealCategoryID; }

        public void setMealCategoryID(Long mealCategoryID) { this.mealCategoryID = mealCategoryID; }

        public String getMealCategoryName() { return mealCategoryName; }

        public void setMealCategoryName(String mealCategoryName) { this.mealCategoryName = mealCategoryName; }
    }
}
