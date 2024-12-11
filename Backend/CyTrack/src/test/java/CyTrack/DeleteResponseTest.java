package CyTrack;

import CyTrack.Responses.Util.DeleteResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteResponseTest {

    // Test the constructor for DeleteResponse
    @Test
    public void testConstructor() {
        String status = "success";
        int code = 200;
        String message = "Deletion successful";

        DeleteResponse response = new DeleteResponse(status, code, message);

        assertEquals(status, response.getStatus());
        assertEquals(code, response.getCode());
        assertEquals(message, response.getMessage());
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        DeleteResponse response = new DeleteResponse("success", 200, "Deletion successful");

        // Test setters and getters for status
        response.setStatus("error");
        assertEquals("error", response.getStatus());

        // Test setters and getters for code
        response.setCode(404);
        assertEquals(404, response.getCode());

        // Test setters and getters for message
        response.setMessage("Deletion failed");
        assertEquals("Deletion failed", response.getMessage());
    }
}
