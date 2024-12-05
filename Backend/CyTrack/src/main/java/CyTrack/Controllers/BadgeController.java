package CyTrack.Controllers;

import CyTrack.Entities.Badge;
import CyTrack.Entities.User;
import CyTrack.Services.BadgeService;
import CyTrack.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/badge")
public class BadgeController {

    private final BadgeService badgeService;
    private final UserService userService;

    @Autowired
    public BadgeController(BadgeService badgeService, UserService userService) {
        this.badgeService = badgeService;
        this.userService = userService;
    }

    // Get all badges
    @Operation(
            summary = "Retrieve all badges",
            description = "Fetches a list of all badges available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All badges retrieved",
                            content = @Content(schema = @Schema(implementation = BadgeResponse.class))
                    )
            }
    )
    @GetMapping("allBadges")
    public ResponseEntity<?> getAllBadges() {
        List<Badge> badges = badgeService.getAllBadges();
        List<BadgeResponse.BadgeData> badgeDataList = badges.stream()
                .map(badge -> new BadgeResponse.BadgeData(badge.getBadgeID(), badge.getBadgeName(), badge.getDescription()))
                .collect(Collectors.toList());
        BadgeResponse response = new BadgeResponse("success", badgeDataList, "All badges retrieved");
        return ResponseEntity.ok(response);
    }

    // Award a badge to a user
    @Operation(
            summary = "Award a badge to a user",
            description = "Assigns a badge to a user based on their user ID and the badge's name.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Badge awarded successfully",
                            content = @Content(schema = @Schema(implementation = BadgeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or badge not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict - Badge already awarded",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "ID of the user to whom the badge will be awarded",
                    required = true,
                    example = "1"
            ),
            @Parameter(
                    name = "badgeName",
                    description = "Name of the badge to be awarded",
                    required = true,
                    example = "Lifetime Achievement"
            )
    })
    @PostMapping("/{userId}/award/{badgeName}")
    public ResponseEntity<?> awardBadgeToUser(@PathVariable Long userId, @PathVariable String badgeName) {
        Optional<User> userOpt = userService.findByUserID(userId);
        Optional<Badge> badgeOpt = badgeService.findByBadgeName(badgeName);

        if (userOpt.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User with ID " + userId + " not found");
            return ResponseEntity.status(404).body(response);
        }
        if (badgeOpt.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 404, "Badge not found", "Badge with name " + badgeName + " not found");
            return ResponseEntity.status(404).body(response);
        }

        boolean awarded = badgeService.awardBadgeToUser(userOpt.get(), badgeOpt.get());
        if (awarded) {
            BadgeResponse response = new BadgeResponse("success", null, "Badge awarded successfully!");
            return ResponseEntity.ok(response);
        } else {
            ErrorResponse response = new ErrorResponse("error", 409, "Conflict", "Badge already awarded to user");
            return ResponseEntity.status(409).body(response);
        }
    }

    // Get all badges a user has earned
    @Operation(
            summary = "Get all badges a user has earned",
            description = "Retrieves a list of badges awarded to a specific user by their user ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User's earned badges retrieved",
                            content = @Content(schema = @Schema(implementation = BadgeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @Parameter(
            name = "userId",
            description = "ID of the user whose earned badges are to be retrieved",
            required = true,
            example = "1"
    )
    @GetMapping("/{userId}/earned")
    public ResponseEntity<?> getUserBadges(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findByUserID(userId);

        if (userOpt.isPresent()) {
            List<Badge> userBadges = badgeService.getUserBadges(userOpt.get());
            List<BadgeResponse.BadgeData> badgeDataList = userBadges.stream()
                    .map(badge -> new BadgeResponse.BadgeData(badge.getBadgeID(), badge.getBadgeName(), badge.getDescription()))
                    .collect(Collectors.toList());
            BadgeResponse response = new BadgeResponse("success", badgeDataList, "User's earned badges retrieved");
            return ResponseEntity.ok(response);
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User with ID " + userId + " not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    /*
    @Test
    public void testGetAllWorkoutsByUserID_Success() throws JSONException {
        // Step 1: Create a test user
        String uniqueUsername = "testUser_" + UUID.randomUUID().toString().substring(0, 5);
        Response userResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"" + uniqueUsername + "\", \"password\": \"password\" }")
                .when()
                .post("/user");

        assertEquals(201, userResponse.getStatusCode());
        Long userID = userResponse.jsonPath().getLong("data.userID");

        // Step 2: Add workouts for the user
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"exerciseType\": \"Running\", \"duration\": 45, \"calories\": 400, \"date\": \"2024-12-04\" }")
                .when()
                .post("/workout/" + userID + "/workout");

        RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"exerciseType\": \"Cycling\", \"duration\": 30, \"calories\": 300, \"date\": \"2024-12-04\" }")
                .when()
                .post("/workout/" + userID + "/workout");

        // Step 3: Retrieve workouts for the user
        Response workoutsResponse = RestAssured.given()
                .when()
                .get("/workout/" + userID + "/workout");

        System.out.println("Here is our current silly output lmao" + workoutsResponse.toString());


        assertEquals(200, workoutsResponse.getStatusCode());

        // Debug raw response
        String rawResponse = workoutsResponse.getBody().asString();
        System.out.println("Raw Response: " + rawResponse);

        // Validate using JSON parsing
        JSONObject jsonResponse = new JSONObject(rawResponse);
        assertEquals("success", jsonResponse.getString("status"));
        assertEquals("Workouts found", jsonResponse.getString("message"));

        JSONArray workouts = jsonResponse.getJSONObject("data").getJSONArray("workouts");
        assertEquals(2, workouts.length());

    }
    *
     */

}
