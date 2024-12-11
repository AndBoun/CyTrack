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
public class MealSystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testMealGET() throws JSONException {
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

        Long createdMealID = mealResponse.jsonPath().getLong("data.mealID");

        // Step 3: Validate the response
        assertEquals(201, mealResponse.getStatusCode());
        try {
            JSONObject jsonResponse = new JSONObject(mealResponse.getBody().asString());
            assertEquals("success", jsonResponse.getString("status"));
            assertNotNull(jsonResponse.getLong("mealId"));
            assertEquals("Meal created", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get Meals for User
        mealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/meal/" + userID + "/meal");

        int statusCode = mealResponse.getStatusCode();
        assertEquals(200, statusCode);
        String returnString2 = mealResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Meals found", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get Meal by ID
        mealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/meal/" + userID + "/meal/" + createdMealID);

        assertEquals(200, mealResponse.getStatusCode());
        String returnString3 = mealResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString3);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Meal found", returnObj.getString("message"));

            JSONObject mealData = returnObj.getJSONArray("data").getJSONObject(0);
            assertEquals(createdMealID, mealData.getLong("mealId"));
            assertEquals("Lunch", mealData.getString("mealName"));
            assertEquals(600, mealData.getInt("calories"));
            assertNotNull(mealData.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get Meals by Date
        String testDate = LocalDate.now().toString();
        mealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/meal/" + userID + "/mealsByDate/" + testDate);

        assertEquals(200, mealResponse.getStatusCode());
        String returnString4 = mealResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString4);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Meals found for date " + testDate, returnObj.getString("message"));

            JSONObject mealData = returnObj.getJSONArray("data").getJSONObject(0);
            assertEquals(createdMealID, mealData.getLong("mealId"));
            assertEquals("Lunch", mealData.getString("mealName"));
            assertEquals(600, mealData.getInt("calories"));
            assertNotNull(mealData.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get Total Nutrients for Date
        mealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/meal/" + userID + "/nutrients/" + testDate);

        assertEquals(200, mealResponse.getStatusCode());
        String returnString5 = mealResponse.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString5);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Total nutrients calculated for date " + testDate, returnObj.getString("message"));

            JSONObject nutrientData = returnObj.getJSONObject("data");
            assertEquals(600, nutrientData.getInt("totalCalories"));
            assertNotNull(nutrientData.getInt("totalProtein"));
            assertNotNull(nutrientData.getInt("totalCarbs"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteMeal_Success() throws JSONException {
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

        Long createdMealID = mealResponse.jsonPath().getLong("data.mealID");

        // Step 3: Validate the response
        assertEquals(201, mealResponse.getStatusCode());
        try {
            JSONObject jsonResponse = new JSONObject(mealResponse.getBody().asString());
            assertEquals("success", jsonResponse.getString("status"));
            assertNotNull(jsonResponse.getLong("mealId"));
            assertEquals("Meal created", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 4: Delete the meal
        Response deleteResponse = RestAssured.given()
                .when()
                .delete("/meal/" + userID + "/meal/" + createdMealID);

        // Validate the delete response
        assertEquals(200, deleteResponse.getStatusCode());
        String deleteResponseBody = deleteResponse.getBody().asString();

        try {
            JSONObject deleteJsonResponse = new JSONObject(deleteResponseBody);
            assertEquals("success", deleteJsonResponse.getString("status"));
            assertEquals(createdMealID, deleteJsonResponse.getLong("data"));
            assertEquals("Meal deleted", deleteJsonResponse.getString("message"));

        }catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 5: Verify the meal no longer exists
        Response verifyResponse = RestAssured.given()
                .when()
                .get("/meal/" + userID + "/meal/" + createdMealID);

        assertEquals(404, verifyResponse.getStatusCode());
        try {
            JSONObject verifyJsonResponse = new JSONObject(verifyResponse.getBody().asString());
            assertEquals("error", verifyJsonResponse.getString("status"));
            assertEquals("Meal not found", verifyJsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateMeal_Success() throws JSONException {
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

        assertEquals(201, mealResponse.getStatusCode());
        Long createdMealID = mealResponse.jsonPath().getLong("data.mealID");

        // Step 3: Validate the creation response
        try {
            JSONObject jsonResponse = new JSONObject(mealResponse.getBody().asString());
            assertEquals("success", jsonResponse.getString("status"));
            assertNotNull(jsonResponse.getLong("mealId"));
            assertEquals("Meal created", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 4: Update the meal with new values
        Response updateResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealName\": \"Updated Lunch\",\n" +
                        "    \"calories\": 700,\n" +
                        "    \"protein\": 40,\n" +
                        "    \"carbs\": 80\n" +
                        "}")
                .when()
                .put("/meal/" + userID + "/meal/" + createdMealID);

        // Step 5: Validate the update response
        assertEquals(200, updateResponse.getStatusCode());
        try {
            JSONObject updateJson = new JSONObject(updateResponse.getBody().asString());
            assertEquals("success", updateJson.getString("status"));
            assertEquals(createdMealID, updateJson.getLong("mealId"));
            assertEquals("Meal updated", updateJson.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Step 6: Fetch the updated meal and validate the changes
        Response getUpdatedMealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/meal/" + userID + "/meal/" + createdMealID);

        assertEquals(200, getUpdatedMealResponse.getStatusCode());
        try {
            JSONObject getUpdatedMealJson = new JSONObject(getUpdatedMealResponse.getBody().asString());
            assertEquals("success", getUpdatedMealJson.getString("status"));

            JSONObject updatedMealData = getUpdatedMealJson.getJSONArray("data").getJSONObject(0);
            assertEquals("Updated Lunch", updatedMealData.getString("mealName"));
            assertEquals(700, updatedMealData.getInt("calories"));
            assertEquals(40, updatedMealData.getInt("protein"));
            assertEquals(80, updatedMealData.getInt("carbs"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
