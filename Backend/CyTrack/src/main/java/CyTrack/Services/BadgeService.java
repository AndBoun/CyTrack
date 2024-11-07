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

        awardInitiateBadge(user);  // Add more badge checks here as needed
        awardIntermediatBadge(user);
        awardAdvancedBadge(user);
        awardStreakBadge(user);
    }

    /**
     * Awards Initiate badge if condition is met.
     * Checks a user's total time.
     * If total time >= 500 (minutes), award badge
     * Otherwise, do nothing
     * @param user we're checking to see if badge criteria is met
     */
    private void awardInitiateBadge(User user) {
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

    private void awardIntermediatBadge(User user) {
        int userTotalTime = user.getTotalTime();

        if (userTotalTime >= 750) {
            if (user.getAge() >= 14 && user.getAge() <= 18) {
                // Check if the user already has this badge
                Optional<Badge> earlyBeginnings = badgeRepository.findByUserAndBadgeName(user, "Early Beginnings");

                if (earlyBeginnings.isEmpty()) {
                    // User does not have the badge, so create and assign it
                    Badge lifetimeTimeBadge = new Badge("Intermediate Gymrat", "Achieved 750 hours", user);
                    badgeRepository.save(lifetimeTimeBadge);
                    user.getBadges().add(lifetimeTimeBadge);
                    userRepository.save(user);
                }
            }
            // Check if the user already has this badge
            Optional<Badge> lifetimeTimeBadgeOpt = badgeRepository.findByUserAndBadgeName(user, "Intermediate Gymrat");

            if (lifetimeTimeBadgeOpt.isEmpty()) {
                // User does not have the badge, so create and assign it
                Badge lifetimeTimeBadge = new Badge("Intermediate Gymrat", "Achieved 750 hours", user);
                badgeRepository.save(lifetimeTimeBadge);
                user.getBadges().add(lifetimeTimeBadge);
                userRepository.save(user);
            }
        }
    }

    private void awardAdvancedBadge(User user) {
        int userTotalTime = user.getTotalTime();

        if (userTotalTime >= 1000) {
            // Check if the user already has this badge
            Optional<Badge> lifetimeTimeBadgeOpt = badgeRepository.findByUserAndBadgeName(user, "Advanced Gymrat");

            if (lifetimeTimeBadgeOpt.isEmpty()) {
                // User does not have the badge, so create and assign it
                Badge lifetimeTimeBadge = new Badge("Advanced Gymrat", "Achieved 1000 hours", user);
                badgeRepository.save(lifetimeTimeBadge);
                user.getBadges().add(lifetimeTimeBadge);
                userRepository.save(user);
            }
        }
    }

    private void awardStreakBadge(User user) {
        int highestStreak = user.getHighestStreak();

        if (highestStreak >= 30) {
            Optional<Badge> streakBadgeOpt = badgeRepository.findByUserAndBadgeName(user, "One Month");

            if (streakBadgeOpt.isEmpty()) {

                Badge streakBadge = new Badge("One Month", "Work out for 1 month straight", user);
                badgeRepository.save(streakBadge);
                user.getBadges().add(streakBadge);
                userRepository.save(user);
            }
        }
    }


    public Optional<Badge> findByBadgeName(String badgeName) {
        return badgeRepository.findByBadgeName(badgeName);
    }
}
