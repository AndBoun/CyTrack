package CyTrack;

import static org.junit.jupiter.api.Assertions.*;

import CyTrack.Entities.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password123");

        // Act
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(user)
                .post("/user");

        // Assert
        assertEquals(201, response.getStatusCode());
    }
}