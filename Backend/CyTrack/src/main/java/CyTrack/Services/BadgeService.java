package CyTrack.Services;


import CyTrack.Entities.Badge;
import CyTrack.Entities.User;
import CyTrack.Repositories.BadgeRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    @Autowired
    public BadgeService(BadgeRepository badgeRepository, UserRepository userRepository) {
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    // Retrieve all badges available in the app
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    // Find a badge by its ID
    public Optional<Badge> findByBadgeId(Long badgeID) {
        return badgeRepository.findByBadgeID(badgeID);
    }

    // Get all badges a specific user has earned
    public List<Badge> getUserBadges(User user) {
        return user.getBadges();
    }

    // Award a badge to a user if the user does not already have it
    public boolean awardBadgeToUser(User user, Badge badge) {
        if (user.getBadges().contains(badge)) {
            // Badge is already awarded to the user
            return false;
        }

        // Add the badge to the user's list of badges and save the user
        user.addBadge(badge);
        userRepository.save(user);
        return true;
    }

    /**
     * Award badges based on conditions met by the user.
     * This method checks all badge conditions for the user.
     * @param user we're checking to see if badge criteria are met
     */
    public void awardEligibleBadges(User user) {
        awardLifetimeBadge(user);  // Add more badge checks here as needed
    }

    /**
     * Awards LifeTimeTimeBadge if condition is met.
     * Checks a user's total time.
     * If total time >= 500 (minutes), award badge
     * Otherwise, do nothing
     * @param user we're checking to see if badge criteria is met
     */

    /*
    public void awardLifetimeBadge(User user) {
        // Check if the user has already earned this badge
        if (user.getBadges().stream().anyMatch(b -> b instanceof LifetimeTimeBadge)) {
            return; // Badge already awarded
        }

        // Create and save the new badge
        LifetimeTimeBadge badge = new LifetimeTimeBadge(user);
        badgeRepository.save(badge);  // Persist the badge
        userRepository.save(user);    // Update user with the new badge
    }
    *
     */



    public void awardLifetimeBadge(User user) {
        int userTotalTime = user.getTotalTime();

        if (userTotalTime >= 500) {
            // Check if the user already has this badge
            Optional<Badge> lifetimeTimeBadgeOpt = badgeRepository.findByUserAndBadgeName(user, "Initiate Gymrat");

            if (lifetimeTimeBadgeOpt.isEmpty()) {
                // User does not have the badge, so create and assign it
                Badge lifetimeTimeBadge = new Badge("Initiate Gymrat", "Achieved 500 hours", user);
                badgeRepository.save(lifetimeTimeBadge);
                user.getBadges().add(lifetimeTimeBadge);
                userRepository.save(user);
            }
        }
    }


    public Optional<Badge> findByBadgeName(String badgeName) {
        return badgeRepository.findByBadgeName(badgeName);
    }
}
