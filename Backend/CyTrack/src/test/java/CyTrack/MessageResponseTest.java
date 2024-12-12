package CyTrack;

import CyTrack.Responses.Social.MessageResponse;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MessageResponseTest {

    // Test the constructor and getters for MessageResponse
    @Test
    public void testConstructor() {
        String status = "success";
        String message = "Message sent successfully";
        MessageResponse.Data data = new MessageResponse.Data("direct", "john_doe", "jane_doe", null, "Hello, Jane!", "12:00 PM", 2L, 1L);

        MessageResponse response = new MessageResponse(status, data, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        MessageResponse.Data responseData = response.getData();
        assertEquals("direct", responseData.getChatType());
        assertEquals("john_doe", responseData.getSenderUsername());
        assertEquals("jane_doe", responseData.getReceiverUsername());
        assertEquals("Hello, Jane!", responseData.getContent());
        assertEquals("12:00 PM", responseData.getTime());
        assertEquals(2L, responseData.getGroupOrReceiverID());
        assertEquals(1L, responseData.getUserID());
    }

    // Test setters and getters
    @Test
    public void testGettersAndSetters() {
        MessageResponse.Data data = new MessageResponse.Data("group", "alice", "bob", "Friends", "Hello everyone!", "10:00 AM", 3L, 1L);

        // Test setters and getters for data fields
        data.setChatType("direct");
        data.setSenderUsername("alice");
        data.setReceiverUsername("bob");
        data.setGroupName("Study Group");
        data.setContent("Study session details.");
        data.setTime("11:00 AM");
        data.setGroupOrReceiverID(4L);
        data.setUserID(2L);

        assertEquals("direct", data.getChatType());
        assertEquals("alice", data.getSenderUsername());
        assertEquals("bob", data.getReceiverUsername());
        assertEquals("Study Group", data.getGroupName());
        assertEquals("Study session details.", data.getContent());
        assertEquals("11:00 AM", data.getTime());
        assertEquals(4L, data.getGroupOrReceiverID());
        assertEquals(2L, data.getUserID());
    }

    // Test Data class constructor
    @Test
    public void testDataConstructor() {
        String chatType = "direct";
        String senderUsername = "charlie";
        String receiverUsername = "dave";
        String groupName = "Sports Group";
        String content = "Let's play soccer tomorrow!";
        String time = "6:00 PM";
        Long groupOrReceiverID = 5L;
        Long userID = 3L;

        MessageResponse.Data data = new MessageResponse.Data(chatType, senderUsername, receiverUsername, groupName, content, time, groupOrReceiverID, userID);

        assertEquals(chatType, data.getChatType());
        assertEquals(senderUsername, data.getSenderUsername());
        assertEquals(receiverUsername, data.getReceiverUsername());
        assertEquals(groupName, data.getGroupName());
        assertEquals(content, data.getContent());
        assertEquals(time, data.getTime());
        assertEquals(groupOrReceiverID, data.getGroupOrReceiverID());
        assertEquals(userID, data.getUserID());
    }


}
