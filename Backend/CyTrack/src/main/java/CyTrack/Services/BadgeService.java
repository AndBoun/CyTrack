package CyTrack.Services;


import CyTrack.Entities.Badges.Badge;
import CyTrack.Entities.Badges.LifetimeTimeBadge;
import CyTrack.Entities.User;
import CyTrack.Repositories.BadgeRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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
        return badgeRepository.findByID(badgeID);
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
        awardTimeBadge(user);  // Add more badge checks here as needed
    }

    /**
     * Awards LifeTimeTimeBadge if condition is met.
     * Checks a user's total time.
     * If total time >= 500 (minutes), award badge
     * Otherwise, do nothing
     * @param user we're checking to see if badge criteria is met
     */
    private void awardTimeBadge(User user) {
        int userTotalTime = user.getTotalTime();

        if (userTotalTime >= 500) {
            Optional<Badge> lifetimeTimeBadgeOpt = badgeRepository.findByBadgeName("LifetimeTimeBadge");

            if (lifetimeTimeBadgeOpt.isPresent()) {
                Badge lifetimeTimeBadge = lifetimeTimeBadgeOpt.get();

                if (!user.getBadges().contains(lifetimeTimeBadge)) {
                    user.getBadges().add(lifetimeTimeBadge);
                    userRepository.save(user);
                }
            } else {
                // Create the badge if it's missing
                Badge lifetimeTimeBadge = new LifetimeTimeBadge(user);  // Assuming constructor exists
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
