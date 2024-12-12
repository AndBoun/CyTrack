package CyTrack;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.Friends;
import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void updateProfilePic() {
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update the user's profile picture
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"userID\": " + userID + ",\n" +
                        "    \"profilePic\": \"profilePic.jpg\"\n" +
                        "}")
                .when()
                .put("/user/profileImage");

        statusCode = response.getStatusCode();

        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("Expected status code", HttpStatus.OK.toString(), returnObj.getString(HttpStatus.OK.toString()));

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    @Test
    public void testSetAndGetAge() {
        User user = new User();
        user.setAge(25);
        assertEquals(25, user.getAge());
    }

    @Test
    public void testSetAndGetHeight() {
        User user = new User();
        user.setHeight(175);
        assertEquals(175, user.getHeight());
    }
    @Test
    public void testSetAndGetWeight() {
        User user = new User();
        user.setWeight(70.5);
        assertEquals(70.5, user.getWeight(), 0.0);
    }
    @Test
    public void testSetAndGetCurrentStreak() {
        User user = new User();
        user.setCurrentStreak(5);
        assertEquals(5, user.getCurrentStreak());
    }
    @Test
    public void testSetAndGetHighestStreak() {
        User user = new User();
        user.setHighestStreak(10);
        assertEquals(10, user.getHighestStreak());
    }
    @Test
    public void testSetAndGetGender() {
        User user = new User();
        user.setGender("Male");
        assertEquals("Male", user.getGender());
    }


    @Test
    public void noExist(){
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"noExistFake\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user/login");

        int statusCode = response.getStatusCode();
        assertEquals(401, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void login(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Login the user
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user/login");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserInfo(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the user's information
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/user/" + userID);

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getUserInfo_Fail(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the user's information
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/user/" + 999999999);

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateUserInformation(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update the user's information
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"userID\": " + userID + ",\n" +
                        "    \"age\": 25,\n" +
                        "    \"height\": 175,\n" +
                        "    \"weight\": 70.5,\n" +
                        "    \"currentStreak\": 5,\n" +
                        "    \"highestStreak\": 10,\n" +
                        "    \"gender\": \"Male\"\n" +
                        "}")
                .when()
                .put("/user/" + userID);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resetPassword(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Reset the user's password
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"userID\": " + userID + ",\n" +
                        "    \"password\": \"newPassword\"\n" +
                        "}")
                .when()
                .put("/user/resetPassword/" + userID);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void resetPasswordUserNotFound(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Reset the user's password
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"userID\": " + 999999999 + ",\n" +
                        "    \"password\": \"newPassword\"\n" +
                        "}")
                .when()
                .put("/user/resetPassword/" + 999999999);

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void deleteUser(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Delete the user
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/user/" + userID);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteUser_fail(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Delete the user
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/user/" + 999999999);

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postResetPassword(){
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long userID = null;
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            userID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Reset the user's password
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"user\": \"" + uniqueUsername + "\"\n" +
                        "}")
                .when()
                .post("/user/resetPassword");

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
