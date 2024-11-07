package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

/*
    *@Author Kai Quach
 */
@Entity
public class UserConversations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long conversationID;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    private String user1_username;
    private String user2_username;

    public UserConversations() {}

    public UserConversations(User user1, String user1_username, User user2, String user2_username) {
        this.user1 = user1;
        this.user1_username = user1_username;
        this.user2 = user2;
        this.user2_username = user2_username;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Long getConversationID() {
        return conversationID;
    }

    public void setConversationID(Long conversationID) {
        this.conversationID = conversationID;
    }

    public String getUser1_username() {
        return user1_username;
    }

    public void setUser1_username(String user1_username) {
        this.user1_username = user1_username;
    }

    public String getUser2_username() {
        return user2_username;
    }

    public void setUser2_username(String user2_username) {
        this.user2_username = user2_username;
    }



}
