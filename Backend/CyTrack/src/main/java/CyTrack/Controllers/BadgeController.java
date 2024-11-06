package CyTrack.Controllers;

import CyTrack.Entities.Badges.Badge;
import CyTrack.Entities.User;
import CyTrack.Services.BadgeService;
import CyTrack.Services.UserService;
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


}
