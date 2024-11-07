package CyTrack.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface detailing general implementation for our Badges
 */
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeID;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    @Column(nullable = false) // Ensure uniqueness for badge names
    private String badgeName;

    @Column(nullable = false)
    private String description;

    public Badge() { }

    public Badge(String badgeName, String description, User user) {
        this.badgeName = badgeName;
        this.description = description;
        this.user = user;
    }

    // Getters and setters for badgeName, description, user, and badgeID
    public String getBadgeName() {
        return badgeName;
    }

    public String getDescription() {
        return description;
    }

    public Long getBadgeID() {
        return badgeID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
