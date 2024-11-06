package CyTrack.Entities.Badges;


import CyTrack.Entities.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface detailing general implementation for our Badges
 */
@Entity
public abstract class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeID;

    @ManyToMany(mappedBy = "badges")
    private List<User> users = new ArrayList<>();

    public Badge(User user) {
        addUser(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getBadges().add(this);
        }
    }

    // Abstract methods to be implemented by subclasses
    public abstract String getBadgeName();
    public abstract String getDescription();

    public Long getBadgeID() {
        return badgeID;
    }
}
