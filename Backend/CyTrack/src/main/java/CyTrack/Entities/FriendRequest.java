package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import io.swagger.v3.oas.annotations.media.Schema;
/*
 *@Author Kai Quach
 * Class for the FriendRequests entity
 */
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_requestid")
    @Schema(description = "ID of the friend request", name = "friendRequestID", required = true, example = "1")
    private Long friendRequestID;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties("sentRequests")
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    @Schema(description = "ID of the sender", name = "sender", required = true, example = "1")
    private User sender;

    @Schema(description = "Username of the sender", name = "sender_username", required = true, example = "user1")
    private String sender_username;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnoreProperties(value = {"workouts", "meals", "friends", "badges", "sentRequests", "receivedRequests"})
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    @Schema(description = "ID of the receiver", name = "receiver", required = true, example = "2")
    private User receiver;

    @Schema(description = "Username of the receiver", name = "receiver_username", required = true, example = "user2")
    private String receiver_username;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status of the friend request", name = "status", required = true, example = "PENDING")
    private RequestStatus status;

    // Default constructor
    public FriendRequest() {
    }

    public FriendRequest(User sender, String sender_username, User receiver, String receiver_username) {
        this.sender = sender;
        this.sender_username = sender_username;
        this.receiver = receiver;
        this.receiver_username = receiver_username;
        this.status = RequestStatus.PENDING;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Long getFriendRequestID() {
        return friendRequestID;
    }

    public void setFriendRequestID(Long friendRequestID) {
        this.friendRequestID = friendRequestID;
    }

    public void setStatus(RequestStatus requestStatus) {
        this.status = requestStatus;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public enum RequestStatus {
        ACCEPTED,
        DECLINED,
        PENDING
    }
}