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

    //=====CONSTRUCTOR=====//

    /**
     * This constructor simply adds an initial user to the list
     * of User entities for our Badge entity
     * @param user
     */
    public Badge(User user) {
        addUser(user);
    }

    //=====GETTERS AND SETTERS=====//


    public List<User> getUsers() {
        return users;
    }


    public void addUser(User user) {
        if (!this.users.contains(user)) {
            this.users.add(user);
            user.getBadges().add(this); // Ensure bidirectional consistency
        }
    }
}
