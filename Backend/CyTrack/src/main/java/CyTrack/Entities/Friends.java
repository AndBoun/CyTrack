package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
 *@Author Kai Quach
 * Class for the Friends entity
 */
@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID of the friend", name = "friendID", required = true, example = "1")
    private Long friendID;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user1_id")
    @JsonIgnore
    @Schema(description = "ID of the first user", name = "user1", required = true, example = "1")
    private User user1;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user2_id")
    @Schema(description = "ID of the second user", name = "user2", required = true, example = "2")
    private User user2;

    @OneToOne
    @JoinColumn(name = "friend_request_id")
    @Schema(description = "Corresponding friend request ID of the two", name = "friendRequest", required = true)
    private FriendRequest friendRequest;

    @Schema(description = "Username of the first user", name = "user1_username", required = true, example = "user1")
    private String user1_username;
    @Schema(description = "Username of the second user", name = "user2_username", required = true, example = "user2")
    private String user2_username;

    public Friends() {}

    public Friends(User user1, String user1_username, User user2, String user2_username) {
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

    public Long getFriendID() {
        return friendID;
    }

    public void setFriendID(Long friendID) {
        this.friendID = friendID;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public Long getFriendRequestID() {
        return friendRequest.getFriendRequestID();
    }

    public User getSender() {
        return friendRequest.getSender();
    }

    public User getReceiver() {
        return friendRequest.getReceiver();
    }

    public void setSender(User sender) {
        friendRequest.setSender(sender);
    }

    public void setReceiver(User receiver) {
        friendRequest.setReceiver(receiver);
    }
}