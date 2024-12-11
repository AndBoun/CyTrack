package CyTrack;

import CyTrack.Responses.User.LoginResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    // Test the constructor for the login response (with userID and message)
    @Test
    public void testConstructorWithUserIDAndMessage() {
        Long userID = 123L;
        String status = "success";
        String message = "Login successful";

        LoginResponse response = new LoginResponse(status, userID, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(userID, response.getData().getUserID(), "UserID should match");
    }

    // Test the constructor for the GET response (with full user details)
    @Test
    public void testConstructorWithFullDetails() {
        Long userID = 123L;
        String status = "success";
        String username = "john_doe";
        String firstName = "John";
        String lastName = "Doe";
        int age = 30;
        int height = 180;
        double weight = 75.5;
        String gender = "Male";
        int streak = 10;
        String imageurl = "http://example.com/profile.jpg";
        String message = "User data retrieved successfully";

        LoginResponse response = new LoginResponse(status, userID, username, firstName, lastName, age, height, weight, gender, streak, imageurl, message);

        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());

        LoginResponse.Data data = response.getData();
        assertNotNull(data, "Data should not be null");
        assertEquals(userID, data.getUserID());
        assertEquals(username, data.getUsername());
        assertEquals(firstName, data.getFirstName());
        assertEquals(lastName, data.getLastName());
        assertEquals(age, data.getAge());
        assertEquals(height, data.getHeight());
        assertEquals(weight, data.getWeight());
        assertEquals(gender, data.getGender());
        assertEquals(streak, data.getStreak());
        assertEquals(imageurl, data.getImageurl());
    }

    // Test the getters and setters for status, message, and data
    @Test
    public void testGettersAndSetters() {
        LoginResponse response = new LoginResponse("success", 123L, "Login successful");

        // Test setters and getters for status and message
        response.setStatus("error");
        response.setMessage("Login failed");

        assertEquals("error", response.getStatus());
        assertEquals("Login failed", response.getMessage());

        // Test setters and getters for data
        LoginResponse.Data data = new LoginResponse.Data(123L);
        response.setData(data);

        assertNotNull(response.getData());
        assertEquals(123L, response.getData().getUserID());
    }

    // Test the Data object and its fields
    @Test
    public void testDataConstructorAndFields() {
        Long userID = 123L;
        String username = "john_doe";
        String firstName = "John";
        String lastName = "Doe";
        int age = 30;
        int height = 180;
        double weight = 75.5;
        String gender = "Male";
        int streak = 10;
        String imageurl = "http://example.com/profile.jpg";

        LoginResponse.Data data = new LoginResponse.Data(userID, username, firstName, lastName, age, height, weight, gender, streak, imageurl);

        assertEquals(userID, data.getUserID());
        assertEquals(username, data.getUsername());
        assertEquals(firstName, data.getFirstName());
        assertEquals(lastName, data.getLastName());
        assertEquals(age, data.getAge());
        assertEquals(height, data.getHeight());
        assertEquals(weight, data.getWeight());
        assertEquals(gender, data.getGender());
        assertEquals(streak, data.getStreak());
        assertEquals(imageurl, data.getImageurl());
    }

    // Test Data setters and getters
    @Test
    public void testDataSettersAndGetters() {
        LoginResponse.Data data = new LoginResponse.Data(123L);

        // Test setters and getters for individual fields
        data.setUsername("john_doe");
        data.setFirstName("John");
        data.setLastName("Doe");
        data.setAge(30);
        data.setHeight(180);
        data.setWeight(75.5);
        data.setGender("Male");
        data.setStreak(10);
        data.setImageurl("http://example.com/profile.jpg");

        assertEquals("john_doe", data.getUsername());
        assertEquals("John", data.getFirstName());
        assertEquals("Doe", data.getLastName());
        assertEquals(30, data.getAge());
        assertEquals(180, data.getHeight());
        assertEquals(75.5, data.getWeight());
        assertEquals("Male", data.getGender());
        assertEquals(10, data.getStreak());
        assertEquals("http://example.com/profile.jpg", data.getImageurl());
    }
}
