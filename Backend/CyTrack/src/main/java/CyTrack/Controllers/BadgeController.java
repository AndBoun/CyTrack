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
    @GetMapping("")
    public ResponseEntity<List<Badge>> getAllBadges() {
        List<Badge> badges = badgeService.getAllBadges();
        return ResponseEntity.ok(badges);
    }

    // Award a badge to a user
    @PostMapping("/{userId}/award/{badgeId}")
    public ResponseEntity<?> awardBadgeToUser(@PathVariable Long userId, @PathVariable Long badgeId) {
        Optional<User> userOpt = userService.findByUserID(userId);
        Optional<Badge> badgeOpt = badgeService.findByBadgeId(badgeId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }
        if (badgeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Badge not found");
        }

        boolean awarded = badgeService.awardBadgeToUser(userOpt.get(), badgeOpt.get());
        if (awarded) {
            return ResponseEntity.ok("Badge awarded successfully!");
        } else {
            return ResponseEntity.status(409).body("Badge already awarded to user");
        }
    }

    // Get all badges a user has earned
    @GetMapping("/{userId}/earned")
    public ResponseEntity<?> getUserBadges(@PathVariable Long userId) {
        Optional<User> userOpt = userService.findByUserID(userId);

        if (userOpt.isPresent()) {
            List<Badge> userBadges = badgeService.getUserBadges(userOpt.get());
            return ResponseEntity.ok(userBadges);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
