package CyTrack;

import CyTrack.Responses.Util.ErrorResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseTest {

    // Test the constructor for ErrorResponse
    @Test
    public void testConstructor() {
        String status = "error";
        int code = 400;
        String message = "Bad Request";
        String details = "The request could not be understood due to malformed syntax.";

        ErrorResponse response = new ErrorResponse(status, code, message, details);

        // Verify the status
        assertEquals(status, response.getStatus());

        // Verify the error object fields
        ErrorResponse.error error = response.getError();
        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertEquals(details, error.getDetails());
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        ErrorResponse response = new ErrorResponse("error", 400, "Bad Request", "Malformed syntax");

        // Test setters and getters for status
        response.setStatus("failure");
        assertEquals("failure", response.getStatus());

        // Test setters and getters for error
        ErrorResponse.error error = new ErrorResponse.error(404, "Not Found", "The resource was not found");
        response.setError(error);

        assertEquals(404, response.getError().getCode());
        assertEquals("Not Found", response.getError().getMessage());
        assertEquals("The resource was not found", response.getError().getDetails());
    }

    // Test error class separately
    @Test
    public void testErrorClass() {
        int code = 500;
        String message = "Internal Server Error";
        String details = "An unexpected condition was encountered";

        ErrorResponse.error error = new ErrorResponse.error(code, message, details);

        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertEquals(details, error.getDetails());
    }
}
