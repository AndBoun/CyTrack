package CyTrack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.UUID;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FriendsSystemTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }


    @Test
    public void testGetFriendsEmpty() throws JSONException {
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0,5);

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

        //get friends
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + userID + "/friends");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            assertEquals("success", returnObj.getString("status"));
            assertEquals("Friends found", returnObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendFriendRequest() throws JSONException{
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

    @Test
    public void sendFriendRequest_alreadySent() throws JSONException{
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
        //Send friend request again
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + user1ID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("error", friendObj.getString("status"));
            assertEquals("Request already sent", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void acceptFriendRequest() throws JSONException {
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
                .put("/" + user2ID + "/request");

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
    }

    @Test
    public void declineFriendRequest() throws JSONException {
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
                .when()
                .delete("/" + user2ID + "/request/" + friendRequestID);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend request declined", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getOutgoing() throws JSONException{
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
        //Get outgoing friend requests
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + user1ID + "/request/outgoing");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();

        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Outgoing friend requests found", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getIngoing() throws JSONException{
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
        //Get outgoing friend requests
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + user2ID + "/request/incoming");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString2 = response.getBody().asString();

        try {
            JSONObject friendObj = new JSONObject(friendString2);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Incoming friend requests found", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeFriend() throws JSONException {
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
                .put("/" + user2ID + "/request");

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

        //Get friends
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + user1ID + "/friends");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);
        Long friendID = null;

        String friendString3 = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString3);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friends found", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            JSONArray friends = data.getJSONArray("friends");
            JSONObject friend = friends.getJSONObject(0);
            friendID = friend.getLong("friendID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Remove friend
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/" + user2ID + "/friends/" + friendID);

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString4 = response.getBody().asString();

        try {
            JSONObject friendObj = new JSONObject(friendString4);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friend removed", friendObj.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void alreadyFriends() {
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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
                .put("/" + user2ID + "/request");

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

        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername2 + "\"\n" +
                        "}")
                .when()
                .post("/" + user1ID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(400, statusCode);
        friendRequestID = null;
        friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("error", friendObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userDoesNotExist(){
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + "SWAGINGTON" + "\"\n" +
                        "}")
                .when()
                .post("/" + user1ID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(404, statusCode);
        Long friendRequestID = null;
        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("error", friendObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    @Test
    public void sendRequestAlready() {
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

        Long user2ID = null;
        statusCode = response.getStatusCode();
        assertEquals(201, statusCode);
        String returnString2 = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString2);
            JSONObject data = returnObj.getJSONObject("data");
            assertEquals("success", returnObj.getString("status"));
            assertEquals("User registered", returnObj.getString("message"));
            user2ID = data.getLong("userID");
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
                .post("/" + user1ID + "/request");

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

        //Send friend request
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"friendUsername\": \"" + uniqueUsername + "\"\n" +
                        "}")
                .when()
                .post("/" + user2ID + "/request");

        statusCode = response.getStatusCode();
        assertEquals(400, statusCode);

        friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("error", friendObj.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    @Test
    public void testGetFriends() {
        // Register the user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
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

        // Get the user's friends
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + userID + "/friends");

        statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String friendString = response.getBody().asString();
        try {
            JSONObject friendObj = new JSONObject(friendString);
            assertEquals("success", friendObj.getString("status"));
            assertEquals("Friends found", friendObj.getString("message"));
            JSONObject data = friendObj.getJSONObject("data");
            assertNotNull(data.getJSONArray("friends"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
