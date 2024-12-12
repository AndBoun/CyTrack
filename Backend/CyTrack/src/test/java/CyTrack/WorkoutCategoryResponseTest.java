package CyTrack;

import CyTrack.Responses.WorkoutCategoryResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorkoutCategoryResponseTest {

    // Test the constructor and getters for WorkoutCategoryResponse
    @Test
    public void testConstructor() {
        String status = "success";
        String message = "Workout categories retrieved successfully";
        List<WorkoutCategoryResponse.WorkoutCategoryData> workoutCategories = Arrays.asList(
                new WorkoutCategoryResponse.WorkoutCategoryData(1L, "Cardio"),
                new WorkoutCategoryResponse.WorkoutCategoryData(2L, "Strength Training")
        );

        WorkoutCategoryResponse response = new WorkoutCategoryResponse(status, workoutCategories, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        WorkoutCategoryResponse.Data responseData = response.getData();
        assertEquals(2, responseData.getWorkoutCategories().size());
        assertEquals("Cardio", responseData.getWorkoutCategories().get(0).getWorkoutCategoryName());
        assertEquals("Strength Training", responseData.getWorkoutCategories().get(1).getWorkoutCategoryName());
    }

    // Test setters and getters for WorkoutCategoryData
    @Test
    public void testGettersAndSetters() {
        WorkoutCategoryResponse.WorkoutCategoryData workoutCategoryData = new WorkoutCategoryResponse.WorkoutCategoryData(3L, "Yoga");

        // Test setters and getters
        workoutCategoryData.setWorkoutCategoryID(4L);
        workoutCategoryData.setWorkoutCategoryName("Pilates");

        assertEquals(4L, workoutCategoryData.getWorkoutCategoryID());
        assertEquals("Pilates", workoutCategoryData.getWorkoutCategoryName());
    }

    // Test constructor for Data class
    @Test
    public void testDataConstructor() {
        List<WorkoutCategoryResponse.WorkoutCategoryData> workoutCategories = Arrays.asList(
                new WorkoutCategoryResponse.WorkoutCategoryData(1L, "Cardio"),
                new WorkoutCategoryResponse.WorkoutCategoryData(2L, "Strength Training")
        );

        WorkoutCategoryResponse.Data data = new WorkoutCategoryResponse.Data(workoutCategories);

        assertNotNull(data.getWorkoutCategories());
        assertEquals(2, data.getWorkoutCategories().size());
        assertEquals("Cardio", data.getWorkoutCategories().get(0).getWorkoutCategoryName());
    }

    // Test WorkoutCategoryResponse with empty workout categories
    @Test
    public void testEmptyWorkoutCategories() {
        String status = "success";
        String message = "No workout categories available";
        List<WorkoutCategoryResponse.WorkoutCategoryData> workoutCategories = Arrays.asList();

        WorkoutCategoryResponse response = new WorkoutCategoryResponse(status, workoutCategories, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        WorkoutCategoryResponse.Data responseData = response.getData();
        assertTrue(responseData.getWorkoutCategories().isEmpty());
    }

    // Test WorkoutCategoryData with invalid data
    @Test
    public void testWorkoutCategoryDataWithInvalidData() {
        WorkoutCategoryResponse.WorkoutCategoryData invalidData = new WorkoutCategoryResponse.WorkoutCategoryData(null, null);

        assertNull(invalidData.getWorkoutCategoryID());
        assertNull(invalidData.getWorkoutCategoryName());
    }
}
