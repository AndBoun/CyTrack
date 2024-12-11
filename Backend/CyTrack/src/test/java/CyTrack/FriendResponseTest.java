package CyTrack;

import CyTrack.Responses.Social.FriendResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

public class FriendResponseTest {

    // Test the constructor and getters for FriendResponse
    @Test
    public void testConstructor() {
        String status = "success";
        String message = "Friends retrieved successfully";
        Long userID = 1L;
        Long friendID = 2L;
        String username = "user1";
        String firstname = "John";

        // Create the FriendsData list
        FriendResponse.FriendsData friendData = new FriendResponse.FriendsData(firstname, username, userID, friendID);
        FriendResponse response = new FriendResponse(status, Arrays.asList(friendData), message);

        // Test main response attributes
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        // Test the inner Data class and its friends list
        FriendResponse.Data responseData = response.getData();
        assertNotNull(responseData);
        assertEquals(1, responseData.getFriends().size());

        // Test individual friend's data
        FriendResponse.FriendsData data = responseData.getFriends().get(0);
        assertEquals(username, data.getUsername());
        assertEquals(firstname, data.getFirstname());
        assertEquals(userID, data.getUserID());
        assertEquals(friendID, data.getFriendID());
    }

    // Test setter methods for FriendResponse
    @Test
    public void testSetters() {
        String status = "success";
        String message = "Friends retrieved successfully";
        Long userID = 1L;
        Long friendID = 2L;
        String username = "user1";
        String firstname = "John";

        // Create the FriendsData list
        FriendResponse.FriendsData friendData = new FriendResponse.FriendsData(firstname, username, userID, friendID);
        FriendResponse response = new FriendResponse(status, Arrays.asList(friendData), message);

        // Modify the data using setters
        String newStatus = "failure";
        response.setStatus(newStatus);
        assertEquals(newStatus, response.getStatus());

        String newMessage = "Error in retrieving friends";
        response.setMessage(newMessage);
        assertEquals(newMessage, response.getMessage());

        // Test setters for Data and FriendsData
        FriendResponse.Data newData = new FriendResponse.Data(Arrays.asList(new FriendResponse.FriendsData("Jane", "user2", 3L, 4L)));
        response.setData(newData);
        assertEquals(1, response.getData().getFriends().size());
    }

    // Test edge case with an empty friends list
    @Test
    public void testEmptyFriendsList() {
        String status = "success";
        String message = "No friends found";

        FriendResponse response = new FriendResponse(status, Arrays.asList(), message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        FriendResponse.Data responseData = response.getData();
        assertNotNull(responseData);
        assertEquals(0, responseData.getFriends().size());
    }

    // Test edge case with null message
    @Test
    public void testNullMessage() {
        String status = "success";
        Long userID = 1L;
        Long friendID = 2L;
        String username = "user1";
        String firstname = "John";

        FriendResponse.FriendsData friendData = new FriendResponse.FriendsData(firstname, username, userID, friendID);
        FriendResponse response = new FriendResponse(status, Arrays.asList(friendData), null);

        assertEquals(status, response.getStatus());
        assertNull(response.getMessage());

        FriendResponse.Data responseData = response.getData();
        assertEquals(1, responseData.getFriends().size());
    }
}
