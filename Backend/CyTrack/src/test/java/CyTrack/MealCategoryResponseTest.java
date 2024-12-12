package CyTrack;

import CyTrack.Responses.MealCategoryResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MealCategoryResponseTest {

    // Test the constructor and getters for MealCategoryResponse
    @Test
    public void testConstructor() {
        String status = "success";
        String message = "Meal categories retrieved successfully";
        List<MealCategoryResponse.MealCategoryData> mealCategories = Arrays.asList(
                new MealCategoryResponse.MealCategoryData(1L, "Breakfast"),
                new MealCategoryResponse.MealCategoryData(2L, "Lunch")
        );

        MealCategoryResponse response = new MealCategoryResponse(status, mealCategories, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        MealCategoryResponse.Data responseData = response.getData();
        assertEquals(2, responseData.getMealCategories().size());
        assertEquals("Breakfast", responseData.getMealCategories().get(0).getMealCategoryName());
        assertEquals("Lunch", responseData.getMealCategories().get(1).getMealCategoryName());
    }

    // Test setters and getters for MealCategoryData
    @Test
    public void testGettersAndSetters() {
        MealCategoryResponse.MealCategoryData mealCategoryData = new MealCategoryResponse.MealCategoryData(3L, "Dinner");

        // Test setters and getters
        mealCategoryData.setMealCategoryID(4L);
        mealCategoryData.setMealCategoryName("Snacks");

        assertEquals(4L, mealCategoryData.getMealCategoryID());
        assertEquals("Snacks", mealCategoryData.getMealCategoryName());
    }

    // Test constructor for Data class
    @Test
    public void testDataConstructor() {
        List<MealCategoryResponse.MealCategoryData> mealCategories = Arrays.asList(
                new MealCategoryResponse.MealCategoryData(1L, "Breakfast"),
                new MealCategoryResponse.MealCategoryData(2L, "Lunch")
        );

        MealCategoryResponse.Data data = new MealCategoryResponse.Data(mealCategories);

        assertNotNull(data.getMealCategories());
        assertEquals(2, data.getMealCategories().size());
        assertEquals("Breakfast", data.getMealCategories().get(0).getMealCategoryName());
    }

    // Test MealCategoryResponse with empty meal categories
    @Test
    public void testEmptyMealCategories() {
        String status = "success";
        String message = "No meal categories available";
        List<MealCategoryResponse.MealCategoryData> mealCategories = Arrays.asList();

        MealCategoryResponse response = new MealCategoryResponse(status, mealCategories, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        MealCategoryResponse.Data responseData = response.getData();
        assertTrue(responseData.getMealCategories().isEmpty());
    }

    // Test MealCategoryData with invalid data
    @Test
    public void testMealCategoryDataWithInvalidData() {
        MealCategoryResponse.MealCategoryData invalidData = new MealCategoryResponse.MealCategoryData(null, null);

        assertNull(invalidData.getMealCategoryID());
        assertNull(invalidData.getMealCategoryName());
    }
}
