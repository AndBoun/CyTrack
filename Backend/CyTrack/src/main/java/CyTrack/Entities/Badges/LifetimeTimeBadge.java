package CyTrack.Entities.Badges;

import CyTrack.Entities.User;
import jakarta.persistence.Entity;

@Entity
public class LifetimeTimeBadge extends Badge{
    private static final String badgeName = "Initiate Gymrat";
    private static final String description = "Spend a cumulative 50 hours working out";

    public LifetimeTimeBadge(User user) {
        super("Initiate Gymrat",
                "Spend a cumulative 50 hours working out",
                user
        );
    }



}
