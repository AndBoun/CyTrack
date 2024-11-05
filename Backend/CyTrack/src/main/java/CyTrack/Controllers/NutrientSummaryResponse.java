package CyTrack.Controllers;

import CyTrack.Services.MealService;

public class NutrientSummaryResponse {
    private String status;
    private Data data;
    private String message;

    public NutrientSummaryResponse(String status, MealService.NutrientSummary nutrients, String message) {
        this.status = status;
        this.data = new Data(nutrients);
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
        private int totalCalories;
        private int totalProtein;
        private int totalCarbs;

        public Data(MealService.NutrientSummary nutrients) {
            this.totalCalories = nutrients.getTotalCalories();
            this.totalProtein = nutrients.getTotalProtein();
            this.totalCarbs = nutrients.getTotalCarbs();
        }

        public int getTotalCalories() {
            return totalCalories;
        }

        public int getTotalProtein() {
            return totalProtein;
        }

        public int getTotalCarbs() {
            return totalCarbs;
        }
    }
}
