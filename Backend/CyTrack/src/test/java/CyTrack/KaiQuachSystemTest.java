package CyTrack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.UUID;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;

import CyTrack.Entities.User;
import CyTrack.Controllers.LoginResponse;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class KaiQuachSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testRegisterUser_Success() throws JSONException {
        // Generate a unique username for the test with a specific length
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
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginUser_Success() throws JSONException {
        // Generate a unique username for the test with a specific length
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

        // Check response body for correct status and message
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Login successful", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterUserExists() throws JSONException {
        // Generate a unique username
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

        // Register the user again
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        statusCode = response.getStatusCode();
        assertEquals(409, statusCode);

        // Check response body for correct status and message
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
            assertEquals("User already exists", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void SendFriendRequest() throws JSONException {
        //Generate a unique username
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        String uniqueUsername2 = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        //Register the user
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");
        Long user1ID = null;
        int statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user1ID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Register the user
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"" + uniqueUsername2 + "\",\n" +
                        "    \"password\": \"password\"\n" +
                        "}")
                .when()
                .post("/user");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + user1ID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);

        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request sent", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }
}

