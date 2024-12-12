package CyTrack;

import CyTrack.Controllers.MealResponse;
import CyTrack.Entities.MealCategory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MealResponseTest {

    // Test the constructor for MealResponse
    @Test
    public void testConstructor() {
        String status = "success";
        String message = "Meals retrieved successfully";

        MealResponse.MealData meal1 = new MealResponse.MealData(
                1L, "Chicken Salad", 300, 25, 10, "12:00", "2024-12-10",
                Collections.singletonList(new MealCategory("Protein")));
        MealResponse.MealData meal2 = new MealResponse.MealData(
                2L, "Pasta", 600, 20, 80, "18:00", "2024-12-10",
                Collections.singletonList(new MealCategory("Carb")));

        List<MealResponse.MealData> meals = Arrays.asList(meal1, meal2);
        MealResponse response = new MealResponse(status, meals, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(meals, response.getData().getMeals(), "Meals list should match");
    }

    // Test getters and setters for MealResponse
    @Test
    public void testGettersAndSetters() {
        MealResponse response = new MealResponse("success", null, "Initial message");

        response.setStatus("error");
        response.setMessage("Updated message");

        assertEquals("error", response.getStatus());
        assertEquals("Updated message", response.getMessage());

        MealResponse.Data data = new MealResponse.Data(Collections.emptyList());
        response.setData(data);

        assertNotNull(response.getData());
        assertEquals(0, response.getData().getMeals().size(), "Data meals list should be empty");
    }

    // Test MealData constructors and fields
    @Test
    public void testMealDataConstructorAndFields() {
        Long mealID = 1L;
        String mealName = "Chicken Salad";
        Integer calories = 300;
        Integer protein = 25;
        Integer carbs = 10;
        String time = "12:00";
        String date = "2024-12-10";
        List<MealCategory> categories = Collections.singletonList(new MealCategory("Protein"));

        MealResponse.MealData mealData = new MealResponse.MealData(mealID, mealName, calories, protein, carbs, time, date, categories);

        assertEquals(mealID, mealData.getMealID());
        assertEquals(mealName, mealData.getMealName());
        assertEquals(calories, mealData.getCalories());
        assertEquals(protein, mealData.getProtein());
        assertEquals(carbs, mealData.getCarbs());
        assertEquals(time, mealData.getTime());
        assertEquals(date, mealData.getDate());
        assertEquals(categories, mealData.getCategories());
    }

    // Test MealData setters and getters
    @Test
    public void testMealDataSettersAndGetters() {
        MealResponse.MealData mealData = new MealResponse.MealData(1L, "", 0, 0, 0, "", "", Collections.emptyList());

        mealData.setMealName("Pasta");
        mealData.setCalories(600);
        mealData.setProtein(20);
        mealData.setCarbs(80);
        mealData.setTime("18:00");
        mealData.setDate("2024-12-10");
        mealData.setCategories(Collections.singletonList(new MealCategory("Carb")));

        assertEquals("Pasta", mealData.getMealName());
        assertEquals(600, mealData.getCalories());
        assertEquals(20, mealData.getProtein());
        assertEquals(80, mealData.getCarbs());
        assertEquals("18:00", mealData.getTime());
        assertEquals("2024-12-10", mealData.getDate());
        assertEquals(1, mealData.getCategories().size());
        assertEquals("Carb", mealData.getCategories().get(0).getMealCategoryName());
    }

    // Test Data class methods
    @Test
    public void testDataClass() {
        MealResponse.Data data = new MealResponse.Data(Collections.emptyList());

        assertTrue(data.getMeals().isEmpty(), "Meals list should be initially empty");

        MealResponse.MealData meal = new MealResponse.MealData(
                1L, "Chicken Salad", 300, 25, 10, "12:00", "2024-12-10",
                Collections.singletonList(new MealCategory("Protein")));
        data.setMeals(Collections.singletonList(meal));

        assertEquals(1, data.getMeals().size(), "Meals list should contain one meal");
        assertEquals(meal, data.getMeals().get(0));
    }
}