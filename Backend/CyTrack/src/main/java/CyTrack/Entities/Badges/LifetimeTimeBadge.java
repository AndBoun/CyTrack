package CyTrack.Entities.Badges;

import CyTrack.Entities.User;
import jakarta.persistence.Entity;

@Entity
public class LifetimeTimeBadge extends Badge{
    private final String badgeName = "Initiate Gymrat";
    private final String description = "Spend a cumulative 50 hours working out";
    //private final int cumulativeTimeRequired = 50; // hours

    // Constructor
    public LifetimeTimeBadge(User user) {
        super(user);
    }

    //=== GETTERS AND SETTERS ===//

    public String getDescription() {
        return description;
    }

    /*
    public int getCumulativeTimeRequired() {
        return cumulativeTimeRequired;
    }
    */



}
