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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WorkoutSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testWorkoutPOST() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create and start a workout for the user
        Response createAndStartResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Running\",\n" +
                        "    \"calories\": 500\n" +
                        "}")
                .when()
                .post("/workout/" + userID + "/createAndStart");

        assertEquals(201, createAndStartResponse.getStatusCode());
        Long workoutID = createAndStartResponse.jsonPath().getLong("data.workoutID");
        assertNotNull(workoutID);

        // Step 3: Start the workout explicitly
        Response startWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/workout/" + userID + "/workout/" + workoutID + "/start");

        assertEquals(200, startWorkoutResponse.getStatusCode());
        assertEquals("success", startWorkoutResponse.jsonPath().getString("status"));

        // Step 4: End the workout
        Response endWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/workout/" + userID + "/workout/" + workoutID + "/end");

        assertEquals(200, endWorkoutResponse.getStatusCode());
        assertEquals("success", endWorkoutResponse.jsonPath().getString("status"));
        assertEquals("Workout ended", endWorkoutResponse.jsonPath().getString("message"));
    }


    @Test
    public void testWorkoutGET() throws JSONException {
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

        Long createdWorkoutID = workoutResponse.jsonPath().getLong("data.workoutID");

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

        // Step 4: Get Workouts for User
        Response getWorkoutsResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workout/" + userID + "/workout");

        assertEquals(200, getWorkoutsResponse.getStatusCode());

        String getResponseBody = getWorkoutsResponse.getBody().asString();
        try {
            JSONObject getJsonResponse = new JSONObject(getResponseBody);
            assertEquals("success", getJsonResponse.getString("status"));
            assertEquals("Workouts found", getJsonResponse.getString("message"));

            assertNotNull(getJsonResponse.getJSONArray("data"));
            assertEquals(1, getJsonResponse.getJSONArray("data").length());

            JSONObject workoutData = getJsonResponse.getJSONArray("data").getJSONObject(0);
            assertEquals("Running", workoutData.getString("exerciseType"));
            assertEquals(45, workoutData.getInt("duration"));
            assertEquals(400, workoutData.getInt("calories"));
            assertEquals("12-04-2024", workoutData.getString("date"));
            assertEquals(createdWorkoutID, workoutData.getLong("workoutID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 5: Get Specific Workout by UserID and WorkoutID
        Response getWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workout/" + userID + "/workout/" + createdWorkoutID);

        assertEquals(200, getWorkoutResponse.getStatusCode());

        String getSpecificResponseBody = getWorkoutResponse.getBody().asString();
        try {
            JSONObject getSpecificJsonResponse = new JSONObject(getSpecificResponseBody);
            assertEquals("success", getSpecificJsonResponse.getString("status"));
            assertEquals("Workout found", getSpecificJsonResponse.getString("message"));

            assertNotNull(getSpecificJsonResponse.getJSONArray("data"));
            assertEquals(1, getSpecificJsonResponse.getJSONArray("data").length());

            JSONObject specificWorkoutData = getSpecificJsonResponse.getJSONArray("data").getJSONObject(0);
            assertEquals("Running", specificWorkoutData.getString("exerciseType"));
            assertEquals(45, specificWorkoutData.getInt("duration"));
            assertEquals(400, specificWorkoutData.getInt("calories"));
            assertEquals("12-04-2024", specificWorkoutData.getString("date"));
            assertEquals(createdWorkoutID, specificWorkoutData.getLong("workoutID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 6: Get Workouts by Date
        Response getWorkoutsByDateResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workout/" + userID + "/workoutByDate/12-04-2024");

        assertEquals(200, getWorkoutsByDateResponse.getStatusCode());

        String getByDateResponseBody = getWorkoutsByDateResponse.getBody().asString();
        try {
            JSONObject getByDateJsonResponse = new JSONObject(getByDateResponseBody);
            assertEquals("success", getByDateJsonResponse.getString("status"));
            assertEquals("Workouts found for 12-04-2024", getByDateJsonResponse.getString("message"));

            assertNotNull(getByDateJsonResponse.getJSONArray("data"));
            assertEquals(1, getByDateJsonResponse.getJSONArray("data").length());

            JSONObject byDateWorkoutData = getByDateJsonResponse.getJSONArray("data").getJSONObject(0);
            assertEquals("Running", byDateWorkoutData.getString("exerciseType"));
            assertEquals(45, byDateWorkoutData.getInt("duration"));
            assertEquals(400, byDateWorkoutData.getInt("calories"));
            assertEquals("12-04-2024", byDateWorkoutData.getString("date"));
            assertEquals(createdWorkoutID, byDateWorkoutData.getLong("workoutID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 7: Get Total Calories by Date
        Response getTotalCaloriesResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workout/" + userID + "/totalCalories/12-04-2024");

        assertEquals(200, getTotalCaloriesResponse.getStatusCode());

        String getTotalCaloriesResponseBody = getTotalCaloriesResponse.getBody().asString();
        try {
            JSONObject getTotalCaloriesJsonResponse = new JSONObject(getTotalCaloriesResponseBody);
            assertEquals("success", getTotalCaloriesJsonResponse.getString("status"));
            assertEquals("Total calories burned for 12-04-2024", getTotalCaloriesJsonResponse.getString("message"));

            assertEquals(400, getTotalCaloriesJsonResponse.getInt("totalCalories"));
            assertEquals(45, getTotalCaloriesJsonResponse.getInt("totalWorkoutTime"));
            assertEquals("12-04-2024", getTotalCaloriesJsonResponse.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateWorkout_Success() throws JSONException {
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

        Long createdWorkoutID = workoutResponse.jsonPath().getLong("data.workoutID");

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

        // Step 4: Update the workout with new values
        Response updateResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"exerciseType\": \"Updated Run\",\n" +
                        "    \"duration\": 50,\n" +
                        "    \"calories\": 410,\n" +
                        "    \"date\": \"12-04-2024\"\n" +
                        "}")
                .when()
                .put("/workout/" + userID + "/workout/" + createdWorkoutID);

        // Step 5: Validate the update response
        assertEquals(200, updateResponse.getStatusCode());
        try {
            JSONObject updateJson = new JSONObject(updateResponse.getBody().asString());
            assertEquals("success", updateJson.getString("status"));
            assertEquals(createdWorkoutID, updateJson.getLong("workoutId"));
            assertEquals("Workout updated", updateJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 6: Fetch the updated workout and validate the changes
        Response getUpdatedWorkoutResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/workout/" + userID + "/workout/" + createdWorkoutID);

        assertEquals(200, getUpdatedWorkoutResponse.getStatusCode());
        try {
            JSONObject getUpdatedWorkoutJson = new JSONObject(getUpdatedWorkoutResponse.getBody().asString());
            assertEquals("success", getUpdatedWorkoutJson.getString("status"));

            JSONObject updatedWorkoutData = getUpdatedWorkoutJson.getJSONArray("data").getJSONObject(0);
            assertEquals("Updated Run", updatedWorkoutData.getString("exerciseType"));
            assertEquals(50, updatedWorkoutData.getInt("duration"));
            assertEquals(410, updatedWorkoutData.getInt("calories"));
            assertEquals("12-04-2024", updatedWorkoutData.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
