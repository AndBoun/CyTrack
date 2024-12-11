package CyTrack;

import CyTrack.Responses.Social.FriendRequestIDResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestIDResponseTest {

    // Test the constructor and getters for FriendRequestIDResponse
    @Test
    public void testConstructor() {
        String status = "success";
        Long friendRequestID = 123L;
        String message = "Friend request ID retrieved successfully";

        FriendRequestIDResponse response = new FriendRequestIDResponse(status, friendRequestID, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        FriendRequestIDResponse.Data responseData = response.getData();
        assertEquals(friendRequestID, responseData.getFriendRequestID());
    }

    // Test setters and getters for Data class
    @Test
    public void testDataConstructorAndSetters() {
        Long friendRequestID = 123L;
        FriendRequestIDResponse.Data data = new FriendRequestIDResponse.Data(friendRequestID);

        // Test setters and getters for Data
        data.setFriendRequestID(456L);

        assertEquals(456L, data.getFriendRequestID());
    }

    // Test with a null friend request ID (edge case)
    @Test
    public void testNullFriendRequestID() {
        String status = "success";
        Long friendRequestID = null;
        String message = "Friend request ID is null";

        FriendRequestIDResponse response = new FriendRequestIDResponse(status, friendRequestID, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        FriendRequestIDResponse.Data responseData = response.getData();
        assertNull(responseData.getFriendRequestID());
    }

    // Test empty message scenario
    @Test
    public void testEmptyMessage() {
        String status = "success";
        Long friendRequestID = 123L;
        String message = "";

        FriendRequestIDResponse response = new FriendRequestIDResponse(status, friendRequestID, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        FriendRequestIDResponse.Data responseData = response.getData();
        assertEquals(friendRequestID, responseData.getFriendRequestID());
    }
}
