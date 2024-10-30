package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendID;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user1_id")
    private User user1;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user2_id")
    private User user2;

    @OneToOne
    @JoinColumn(name = "friend_request_id")
    private FriendRequest friendRequest;

    public Friends() {}

    public Friends(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
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