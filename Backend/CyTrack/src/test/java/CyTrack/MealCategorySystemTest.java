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
public class MealCategorySystemTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void tesAddMealCategoryByUserID() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a mealCategory for the user
        Response mealCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/mealCategory/" + userID + "/mealCategory");

        // Step 3: Validate the response
        assertEquals(200, mealCategoryResponse.getStatusCode());
        String responseBody = mealCategoryResponse.getBody().asString();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meal category added successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddMealToMealCategoryByUserID() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a mealCategory for the user
        Response mealCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/mealCategory/" + userID + "/mealCategory");

        System.out.println("Meal Category Response: " + mealCategoryResponse.getBody().asString());
        assertEquals(200, mealCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long mealCategoryId = mealCategoryResponse.jsonPath().getLong("data.mealCategories[0].mealCategoryID");
        System.out.println("Meal Category ID: " + mealCategoryId);

        // Step 3: Create a meal for the user
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
        Long mealId = mealResponse.jsonPath().getLong("data.mealID");

        // Step 4: Add the meal to the meal category
        Response addMealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/mealCategory/" + mealCategoryId + "/addMeal/" + mealId);

        assertEquals(200, addMealResponse.getStatusCode());

        String addMealResponseBody = addMealResponse.getBody().asString();
        try {
            JSONObject jsonResponse = new JSONObject(addMealResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meal added to category successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetmealCategoriesByUser() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a mealCategory for the user
        Response mealCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/mealCategory/" + userID + "/mealCategory");

        // Step 3: Validate the response
        assertEquals(200, mealCategoryResponse.getStatusCode());
        String responseBody = mealCategoryResponse.getBody().asString();

        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meal category added successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testGetmealsByMealCategory() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a mealCategory for the user
        Response mealCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/mealCategory/" + userID + "/mealCategory");

        System.out.println("Meal Category Response: " + mealCategoryResponse.getBody().asString());
        assertEquals(200, mealCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long mealCategoryId = mealCategoryResponse.jsonPath().getLong("data.mealCategories[0].mealCategoryID");
        System.out.println("Meal Category ID: " + mealCategoryId);

        // Step 3: Create a meal for the user
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
        Long mealId = mealResponse.jsonPath().getLong("data.mealID");

        // Step 4: Add the meal to the meal category
        Response addMealResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .post("/mealCategory/" + mealCategoryId + "/addMeal/" + mealId);

        assertEquals(200, addMealResponse.getStatusCode());

        String addMealResponseBody = addMealResponse.getBody().asString();
        try {
            JSONObject jsonResponse = new JSONObject(addMealResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meal added to category successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Step 5: Get meal by meal category
        Response getMealsResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/mealCategory/" + userID + "/mealCategory/" + mealCategoryId);

        assertEquals(200, getMealsResponse.getStatusCode());

        String getMealsResponseBody = getMealsResponse.getBody().asString();
        System.out.println("Get Meals Response: " + getMealsResponseBody);

        try {
            JSONObject jsonResponse = new JSONObject(getMealsResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meals retrieved successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteMealCategory() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());

        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Create a mealCategory for the user
        Response mealCategoryResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"mealCategoryName\": \"Test Category\"\n" +
                        "}")
                .when()
                .post("/mealCategory/" + userID + "/mealCategory");

        System.out.println("Meal Category Response: " + mealCategoryResponse.getBody().asString());
        assertEquals(200, mealCategoryResponse.getStatusCode());

        // Corrected JSON path
        Long mealCategoryId = mealCategoryResponse.jsonPath().getLong("data.mealCategories[0].mealCategoryID");
        System.out.println("Meal Category ID: " + mealCategoryId);

        // Step 3: Delete mealCategory for the user
        Response deleteResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/mealCategory/" + userID + "/mealCategory/" + mealCategoryId);

        assertEquals(200, deleteResponse.getStatusCode());

        String deleteResponseBody = deleteResponse.getBody().asString();
        System.out.println("Delete Response: " + deleteResponseBody);

        try {
            JSONObject jsonResponse = new JSONObject(deleteResponseBody);
            assertEquals("success", jsonResponse.getString("status"));
            assertEquals("Meal category deleted successfully", jsonResponse.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
