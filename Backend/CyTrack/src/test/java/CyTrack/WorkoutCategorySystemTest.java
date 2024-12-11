package CyTrack;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WorkoutCategorySystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void tesAddWorkoutCategoryByUserID() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workoutCategory for the user
        Response workoutCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"workoutCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/workoutCategory/" + userID + "/workoutCategory");

        // Step 3: Validate the response
        assertEquals(200, workoutCategoryResponse.getStatusCode());
        String responseBody = workoutCategoryResponse.getBody().asString();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout category added successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddWorkoutToWorkoutCategoryByUserID() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workoutCategory for the user
        Response workoutCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"workoutCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/workoutCategory/" + userID + "/workoutCategory");

        System.out.println("Workout Category Response: " + workoutCategoryResponse.getBody().asString());
        assertEquals(200, workoutCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long workoutCategoryId = workoutCategoryResponse.jsonPath().getLong("data.workoutCategories[0].workoutCategoryID");
        System.out.println("Workout Category ID: " + workoutCategoryId);

        // Step 3: Create a workout for the user
        Response workoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"calories\": 500\n" +
                        "}")
                .when()
                .post("/workout/" + userID + "/workout");

        assertEquals(201, workoutResponse.getStatusCode());
        Long workoutId = workoutResponse.jsonPath().getLong("data.workoutID");

        // Step 4: Add the workout to the workout category
        Response addWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/workoutCategory/" + workoutCategoryId + "/addWorkout/" + workoutId);

        assertEquals(200, addWorkoutResponse.getStatusCode());

        String addWorkoutResponseBody = addWorkoutResponse.getBody().asString();
        try {
            JSONObject jsonResponse = new JSONObject(addWorkoutResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout added to category successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetworkoutCategoriesByUser() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workoutCategory for the user
        Response workoutCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"workoutCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/workoutCategory/" + userID + "/workoutCategory");

        // Step 3: Validate the response
        assertEquals(200, workoutCategoryResponse.getStatusCode());
        String responseBody = workoutCategoryResponse.getBody().asString();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout category added successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetworkoutsByWorkoutCategory() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workoutCategory for the user
        Response workoutCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"workoutCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/workoutCategory/" + userID + "/workoutCategory");

        System.out.println("Workout Category Response: " + workoutCategoryResponse.getBody().asString());
        assertEquals(200, workoutCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long workoutCategoryId = workoutCategoryResponse.jsonPath().getLong("data.workoutCategories[0].workoutCategoryID");
        System.out.println("Workout Category ID: " + workoutCategoryId);

        // Step 3: Create a workout for the user
        Response workoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"calories\": 500\n" +
                        "}")
                .when()
                .post("/workout/" + userID + "/workout");

        assertEquals(201, workoutResponse.getStatusCode());
        Long workoutId = workoutResponse.jsonPath().getLong("data.workoutID");

        // Step 4: Add the workout to the workout category
        Response addWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/workoutCategory/" + workoutCategoryId + "/addWorkout/" + workoutId);

        assertEquals(200, addWorkoutResponse.getStatusCode());

        String addWorkoutResponseBody = addWorkoutResponse.getBody().asString();
        try {
            JSONObject jsonResponse = new JSONObject(addWorkoutResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout added to category successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Step 5: Get workout by workout category
        Response getWorkoutsResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workoutCategory/" + userID + "/workoutCategory/" + workoutCategoryId);

        assertEquals(200, getWorkoutsResponse.getStatusCode());

        String getWorkoutsResponseBody = getWorkoutsResponse.getBody().asString();
        System.out.println("Get Workouts Response: " + getWorkoutsResponseBody);

        try {
            JSONObject jsonResponse = new JSONObject(getWorkoutsResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workouts retrieved successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteWorkoutCategory() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workoutCategory for the user
        Response workoutCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"workoutCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/workoutCategory/" + userID + "/workoutCategory");

        System.out.println("Workout Category Response: " + workoutCategoryResponse.getBody().asString());
        assertEquals(200, workoutCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long workoutCategoryId = workoutCategoryResponse.jsonPath().getLong("data.workoutCategories[0].workoutCategoryID");
        System.out.println("Workout Category ID: " + workoutCategoryId);

        // Step 3: Delete workoutCategory for the user
        Response deleteResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/workoutCategory/" + userID + "/workoutCategory/" + workoutCategoryId);

        assertEquals(200, deleteResponse.getStatusCode());

        String deleteResponseBody = deleteResponse.getBody().asString();
        System.out.println("Delete Response: " + deleteResponseBody);

        try {
            JSONObject jsonResponse = new JSONObject(deleteResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout category deleted successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
