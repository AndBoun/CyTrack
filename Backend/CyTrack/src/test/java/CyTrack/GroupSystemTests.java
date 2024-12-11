package CyTrack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.UUID;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GroupSystemTests {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testCreateGroup_Success() throws JSONException {
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

        // Generate a unique group name for the test with a specific length
        String uniqueGroupName = "testGroup_" + UUID.randomUUID().toString().substring(0, 5);

        // Create the group
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupName\": \"" + uniqueGroupName + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/groupchat/create");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group chat created successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
//
//    @Test
//    public void testAddMember_Success() throws JSONException {
//        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
//
//        // Register the user
//        Response response = RestAssured.given()
//                .header("Content-Type", "application/json")
//                .body("{\n" +
//                        "    \"username\": \"" + uniqueUsername + "\",\n" +
//                        "    \"password\": \"password\"\n" +
//                        "}")
//                .when()
//                .post("/user");
//
//        int statusCode = response.getStatusCode();
//        assertEquals(201, statusCode);
//        Long userID = null;
//        String returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            JSONObject data = returnObj.getJSONObject("data");
//            assertEquals("success", returnObj.getString("status"));
//            assertEquals("User registered", returnObj.getString("message"));
//            userID = data.getLong("userID");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Generate a unique group name for the test with a specific length
//        String uniqueGroupName = "testGroup_" + UUID.randomUUID().toString().substring(0, 5);
//
//        // Create the group
//        response = RestAssured.given()
//                .header("Content-Type", "application/json")
//                .body("{\n" +
//                        "    \"groupName\": \"" + uniqueGroupName + "\"\n" +
//                        "}")
//                .when()
//                .post("/" + userID + "/groupchat/create");
//
//        statusCode = response.getStatusCode();
//        assertEquals(201, statusCode);
//        Long groupChatID = null;
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            JSONObject data = returnObj.getJSONObject("data");
//            assertEquals("success", returnObj.getString("status"));
//            assertEquals("Group chat created successfully", returnObj.getString("message"));
//            groupChatID = data.getLong("groupID");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Generate a unique username for the test with a specific length
//        String uniqueUsername2 = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
//
//        // Register the user
//        response = RestAssured.given()
//                .header("Content-Type", "application/json")
//                .body("{\n" +
//                        "    \"username\": \"" + uniqueUsername2 + "\",\n" +
//                        "    \"password\": \"password\"\n" +
//                        "}")
//                .when()
//                .post("/user");
//
//        statusCode = response.getStatusCode();
//        assertEquals(201, statusCode);
//        Long memberID = null;
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            JSONObject data = returnObj.getJSONObject("data");
//            assertEquals("success", returnObj.getString("status"));
//            assertEquals("User registered", returnObj.getString("message"));
//            memberID = data.getLong("userID");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Add the member to the group
//        response = RestAssured.given()
//                .header("Content-Type", "application/json")
//                .body("{\n" +
//                        "    \"groupChatID\": " + groupChatID + ",\n" +
//                        "    \"userID\": " + memberID + "\n" +
//                        "}")
//                .when()
//                .post("/" + userID + "/groupchat/addMember");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.getString("status"));
//            assertEquals("Member added successfully", returnObj.getString("message"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Get the group chat to verify the member was added
//        response = RestAssured.given()
//                .header("Content-Type", "application/json")
//                .body("{\n" +
//                        "    \"groupChatID\": " + groupChatID + "\n" +
//                        "}")
//                .when()
//                .get(userID + "/groupchat/getMembers");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.getString("status"));
//            assertEquals("Members retrieved successfully", returnObj.getString("message"));
//            JSONArray members = returnObj.getJSONObject("data").getJSONArray("members");
//            assertEquals(2, members.length());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void testAddMember_Error() throws JSONException {
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

        // Generate a unique group name for the test with a specific length
        String uniqueGroupName = "testGroup_" + UUID.randomUUID().toString().substring(0, 5);

        // Create the group
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupName\": \"" + uniqueGroupName + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/groupchat/create");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long groupChatID = null;
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group chat created successfully", returnObj.getString("message"));
            groupChatID = data.getLong("groupID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Generate a unique username for the test with a specific length
        String uniqueUsername2 = "testUser_" + UUID.randomUUID().toString().substring(0, 5);

        // Register the user
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
        Long memberID = null;
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            memberID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Add the member to the group
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + ",\n" +
                        "    \"userID\": " + memberID + "\n" +
                        "}")
                .when()
                .post("/" + userID + "/groupchat/addMember");

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
            assertEquals("Group chat or user not found or not friends or already a member", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
