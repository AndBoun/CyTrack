package CyTrack.Entities.Badges;

import jakarta.persistence.Entity;

@Entity
public class LifetimeTimeBadge extends Badge{
    private String description = "Spend a cumulative 50 hours working out";
    private int cumulativeTimeRequired = 50; // hours

    private boolean isEarned;


    public LifetimeTimeBadge (){

    }

    //===GETTERS AND SETTERS===//

}
