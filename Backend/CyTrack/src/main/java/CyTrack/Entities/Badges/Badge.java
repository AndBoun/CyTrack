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

    // Getters and Setters for users
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    // Additional badge-specific fields and methods
}
