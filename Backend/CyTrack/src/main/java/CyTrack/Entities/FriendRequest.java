package CyTrack.Entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendRequestID;

    @ManyToOne
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("sender")
    private User sender;

    @ManyToOne
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("receiver")
    private User receiver;

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    public FriendRequest(){

    }

    public FriendRequest(User sender, User receiver){
        this.sender = sender;
        this.receiver = receiver;
        this.status = RequestStatus.PENDING;
    }

    public User getSender(){
        return sender;
    }

    public User getReceiver(){
        return receiver;
    }

    public void setSender(User sender){
        this.sender = sender;
    }

    public void setReceiver(User receiver){
        this.receiver = receiver;
    }

    public Long getFriendRequestID(){
        return friendRequestID;
    }

    public void setFriendRequestID(Long friendRequestID){
        this.friendRequestID = friendRequestID;
    }

    public enum RequestStatus {
        ACCEPTED,
        DECLINED,
        PENDING
    }

}
