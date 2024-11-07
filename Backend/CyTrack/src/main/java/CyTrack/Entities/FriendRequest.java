package CyTrack.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/*
 *@Author Kai Quach
 * Class for the FriendRequests entity
 */
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_requestid")
    private Long friendRequestID;


    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("sentRequests")
    @JoinColumn(name = "sender_id")
    private User sender;


    private String sender_username;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("receivedRequests")
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String receiver_username;

    @Enumerated(EnumType.STRING)
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