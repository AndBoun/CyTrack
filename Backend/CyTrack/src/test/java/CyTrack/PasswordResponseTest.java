package CyTrack;

import CyTrack.Responses.User.passwordResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordResponseTest {

    // Test the constructor without data (no userID)
    @Test
    public void testConstructorWithoutData() {
        passwordResponse response = new passwordResponse("success", "Password updated successfully");

        assertEquals("success", response.getStatus());
        assertEquals("Password updated successfully", response.getMessage());
        assertNull(response.getData(), "Data should be null");
    }

    // Test the constructor with userID (data included)
    @Test
    public void testConstructorWithUserID() {
        Long userID = 123L;
        passwordResponse response = new passwordResponse("success", "Password updated successfully", userID);

        assertEquals("success", response.getStatus());
        assertEquals("Password updated successfully", response.getMessage());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(userID, response.getData().getUserID());
        assertNull(response.getData().getPassword(), "Password should be null by default");
    }

    // Test getter and setter methods for status and message
    @Test
    public void testGettersAndSetters() {
        passwordResponse response = new passwordResponse("success", "Password updated successfully");

        response.setStatus("error");
        response.setMessage("Password update failed");

        assertEquals("error", response.getStatus());
        assertEquals("Password update failed", response.getMessage());
    }

    // Test the setter and getter methods for Data
    @Test
    public void testSetData() {
        passwordResponse response = new passwordResponse("success", "Password updated successfully");

        Long userID = 123L;
        passwordResponse.Data data = new passwordResponse.Data(userID);
        response.setData(data);

        assertEquals(userID, response.getData().getUserID());
        assertNull(response.getData().getPassword(), "Password should be null by default");

        // Set a password to the Data object
        response.getData().setPassword("newPassword123");

        assertEquals("newPassword123", response.getData().getPassword());
    }
}
