//package CyTrack;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import CyTrack.Entities.User;
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
//public class KaiQuachSystemTest {
//    @LocalServerPort
//    int port;
//
//    @BeforeEach
//    public void setUp() {
//        RestAssured.port = port;
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @Test
//    public void testRegisterUser_Success() {
//        // Arrange
//        User user = new User();
//        user.setUsername("testUser");
//        user.setPassword("password123");
//
//        // Act
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .body(user)
//                .post("/user");
//
//        // Assert
//        assertEquals(201, response.getStatusCode());
//    }
//
//    @Test
//    public void testGetUserById_Success() {
//        // Arrange
//        Long userId = 1L; // Assuming a user with ID 1 exists
//
//        // Act
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .get("/user/" + userId);
//
//        // Assert
//        assertEquals(201, response.getStatusCode());
//        assertNotNull(response.getBody().asString());
//    }
//
//    @Test
//    public void testUpdateUser_Success() {
//        // Arrange
//        Long userId = 1L; // Assuming a user with ID 1 exists
//        User updatedUser = new User();
//        updatedUser.setFirstName("UpdatedFirstName");
//        updatedUser.setLastName("UpdatedLastName");
//
//        // Act
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .body(updatedUser)
//                .put("/user/" + userId);
//
//        // Assert
//        assertEquals(200, response.getStatusCode());
//        assertNotNull(response.getBody().asString());
//    }
//
//    @Test
//    public void testDeleteUser_Success() {
//        // Arrange
//        Long userId = 1L; // Assuming a user with ID 1 exists
//
//        // Act
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .delete("/user/" + userId);
//
//        // Assert
//        assertEquals(200, response.getStatusCode());
//        assertNotNull(response.getBody().asString());
//    }
//
//    @Test
//    public void testLoginUser_Success() {
//        // Arrange
//        User user = new User();
//        user.setUsername("testUser");
//        user.setPassword("password123");
//
//        // Act
//        Response response = RestAssured.given()
//                .contentType("application/json")
//                .body(user)
//                .post("/user/login");
//
//        // Assert
//        assertEquals(201, response.getStatusCode());
//        assertNotNull(response.getBody().asString());
//    }
//}
