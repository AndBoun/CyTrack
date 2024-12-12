package CyTrack;

import CyTrack.Controllers.WorkoutResponse;
import io.restassured.RestAssured;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import io.restassured.response.Response;


import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class EduardoBarbozaSystemTest {
    @LocalServerPort
    int port;

    //silly port and url setup
    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testCreateWorkout_Success() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workout for the user
        Response workoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"duration\": 45,\n" +
                        "    \"calories\": 400,\n" +
                        "    \"date\": \"12-04-2024\"\n" +
                        "}")
                .when()
                .post("/workout/" + userID + "/workout");

        // Step 3: Validate the response
        assertEquals(201, workoutResponse.getStatusCode());
        String responseBody = workoutResponse.getBody().asString();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Workout created", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateWorkout_UserNotFound() throws JSONException{
        // Use a non-existent userID
        Long invalidUserID = 9999L;

        // Attempt to create a workout
        Response workoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"duration\": 45,\n" +
                        "    \"calories\": 400,\n" +
                        "    \"date\": \"12-04-2024\"\n" +
                        "}")
                .when()
                .post("/workout/" + invalidUserID + "/workout");

        // Validate the response
        assertEquals(404, workoutResponse.getStatusCode());
        String responseBody = workoutResponse.getBody().asString();
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("error", jsonResponse.getString("status"));
            assertEquals(404, jsonResponse.getInt("code"));
            assertEquals("User not found", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCreateMeal_Success() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a meal for the user
        Response mealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealType\": \"Lunch\",\n" +
                        "    \"calories\": 600,\n" +
                        "    \"date\": \"" + LocalDate.now() + "\"\n" +
                        "}")
                .when()
                .post("/meal/" + userID + "/meal");

        // Step 3: Validate the response
        assertEquals(201, mealResponse.getStatusCode());
        try {
            JSONObject jsonResponse = new JSONObject(mealResponse.getBody().asString());
            assertEquals("success", jsonResponse.getString("status"));
            assertNotNull(jsonResponse.getLong("mealId"));
            assertEquals("Meal created", jsonResponse.getString("message"));
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteWorkout_Success() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a workout for the user
        Response workoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"duration\": 45,\n" +
                        "    \"calories\": 400,\n" +
                        "    \"date\": \"12-04-2024\"\n" +
                        "}")
                .when()
                .post("/workout/" + userID + "/workout");

        // Step 3: Validate the response
        assertEquals(201, workoutResponse.getStatusCode());

        Long workoutID = workoutResponse.jsonPath().getLong("data.workoutID");

        // Step 4: Delete the workout
        Response deleteResponse = RestAssured.given()
                .when()
                .delete("/workout/" + userID + "/workout/" + workoutID);

        // Validate the delete response
        assertEquals(200, deleteResponse.getStatusCode());
        String deleteResponseBody = deleteResponse.getBody().asString();

        try {
            JSONObject deleteJsonResponse = new JSONObject(deleteResponseBody);
            assertEquals("success", deleteJsonResponse.getString("status"));
            assertEquals(workoutID, deleteJsonResponse.getLong("data"));
            assertEquals("Workout deleted", deleteJsonResponse.getString("message"));

        }catch (JSONException e) {
            e.printStackTrace();
        }


        // Step 5: Verify the workout no longer exists
        Response verifyResponse = RestAssured.given()
                .when()
                .get("/workout/" + userID + "/workout/" + workoutID);

        assertEquals(404, verifyResponse.getStatusCode());
        try {
            JSONObject verifyJsonResponse = new JSONObject(verifyResponse.getBody().asString());
            assertEquals("error", verifyJsonResponse.getString("status"));
            assertEquals("Workout not found", verifyJsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
