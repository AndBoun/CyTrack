package CyTrack.Entities.Badges;


import CyTrack.Entities.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface detailing general implementation for our Badges
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "badge_type")
public abstract class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeID;

    @ManyToMany(mappedBy = "badges")
    private List<User> users = new ArrayList<>();


    @Column(nullable = false, unique = true) // Ensure uniqueness for badge names
    private String badgeName;

    @Column(nullable = false)
    private String description;


    public Badge(String badgeName, String description, User user) {
        this.badgeName = badgeName;
        this.description = description;
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

    public String getBadgeName() {
        return badgeName;
    }

    public String getDescription() {
        return description;
    }

    public Long getBadgeID() {
        return badgeID;
    }
}
