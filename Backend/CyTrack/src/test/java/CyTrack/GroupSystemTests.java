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

    @Test
    public void testAddMember_Success() throws JSONException {
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
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long friendRequestID = null;
        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request sent", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            friendRequestID = data.getLong("friendRequestID");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Accept friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(
                        "{\n" +
                                "    \"friendRequestID\": \"" + friendRequestID + "\"\n" +
                                "}")
                .when()
                .put("/" + memberID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request accepted", friendObj.getString("message"));
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
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Member added successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the group chat to verify the member was added
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + "\n" +
                        "}")
                .when()
                .get(userID + "/groupchat/getMembers");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Members retrieved successfully", returnObj.getString("message"));
            JSONArray members = returnObj.getJSONObject("data").getJSONArray("members");
            assertEquals(2, members.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveMember_Success() throws JSONException {
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
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long friendRequestID = null;
        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request sent", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            friendRequestID = data.getLong("friendRequestID");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Accept friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(
                        "{\n" +
                                "    \"friendRequestID\": \"" + friendRequestID + "\"\n" +
                                "}")
                .when()
                .put("/" + memberID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request accepted", friendObj.getString("message"));
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
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Member added successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the group chat to verify the member was added
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + "\n" +
                        "}")
                .when()
                .get(userID + "/groupchat/getMembers");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Members retrieved successfully", returnObj.getString("message"));
            JSONArray members = returnObj.getJSONObject("data").getJSONArray("members");
            assertEquals(2, members.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Remove the member from the group

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + ",\n" +
                        "    \"userID\": " + memberID + "\n" +
                        "}")
                .when()
                .delete("/" + userID + "/groupchat/removeMember");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Member removed successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemoveMember_Failure() throws JSONException {
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
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long friendRequestID = null;
        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request sent", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            friendRequestID = data.getLong("friendRequestID");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Accept friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(
                        "{\n" +
                                "    \"friendRequestID\": \"" + friendRequestID + "\"\n" +
                                "}")
                .when()
                .put("/" + memberID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request accepted", friendObj.getString("message"));
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
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Member added successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the group chat to verify the member was added
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + "\n" +
                        "}")
                .when()
                .get(userID + "/groupchat/getMembers");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Members retrieved successfully", returnObj.getString("message"));
            JSONArray members = returnObj.getJSONObject("data").getJSONArray("members");
            assertEquals(2, members.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Remove the member from the group

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + ",\n" +
                        "    \"userID\": " + userID + "\n" +
                        "}")
                .when()
                .delete("/" + memberID + "/groupchat/removeMember");

        statusCode = response.getStatusCode();
        assertEquals(403, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
            assertEquals("Only the admin can remove members", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testRemoveMember_Failure2() throws JSONException {
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
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + userID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        Long friendRequestID = null;
        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request sent", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            friendRequestID = data.getLong("friendRequestID");
        } catch (JSONException e) {
            e.printStackTrace();

        }
        //Accept friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(
                        "{\n" +
                                "    \"friendRequestID\": \"" + friendRequestID + "\"\n" +
                                "}")
                .when()
                .put("/" + memberID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request accepted", friendObj.getString("message"));
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
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Member added successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the group chat to verify the member was added
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupChatID + "\n" +
                        "}")
                .when()
                .get(userID + "/groupchat/getMembers");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Members retrieved successfully", returnObj.getString("message"));
            JSONArray members = returnObj.getJSONObject("data").getJSONArray("members");
            assertEquals(2, members.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Remove the member from the group

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + 1000000000 + ",\n" +
                        "    \"userID\": " + memberID + "\n" +
                        "}")
                .when()
                .delete("/" + userID + "/groupchat/removeMember");

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
            assertEquals("Group chat or user not found", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
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

    @Test
    public void testUpdateGroup_Success() throws JSONException {
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
        Long groupID = null;
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group chat created successfully", returnObj.getString("message"));
            groupID = data.getLong("groupID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update the group name
        String updatedGroupName = "updatedGroup_" + UUID.randomUUID().toString().substring(0, 5);

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupID + ",\n" +
                        "    \"groupName\": \"" + updatedGroupName + "\"\n" +
                        "}")
                .when()
                .put("/" + userID + "/groupchat/updateGroupName");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group name updated successfully", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateGroup_Failure() throws JSONException {
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
        Long groupID = null;
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group chat created successfully", returnObj.getString("message"));
            groupID = data.getLong("groupID");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Update the group name
        String updatedGroupName = "updatedGroup_" + UUID.randomUUID().toString().substring(0, 5);

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"groupChatID\": " + groupID + ",\n" +
                        "    \"groupName\": \"" + updatedGroupName + "\"\n" +
                        "}")
                .when()
                .put("/" + 69 + "/groupchat/updateGroupName");

        statusCode = response.getStatusCode();
        assertEquals(403, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("error", returnObj.getString("status"));
            assertEquals("Only the admin can update the group name", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllGroups_Success() throws JSONException {
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

        // Get the all groups

        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + userID + "/groupchat");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Group chats retrieved successfully", returnObj.getString("message"));
            JSONArray groups = returnObj.getJSONObject("data").getJSONArray("groups");
            assertEquals(1, groups.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
