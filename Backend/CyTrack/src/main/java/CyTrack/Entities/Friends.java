package CyTrack.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Friends {
    // =============================== Fields ================================== //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Primary key
    private Long friendID;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user1")
    private User user1;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty("user2")
    private User user2;


    // =============================== Constructors ================================== //
    public Friends() {

    }

    public Friends(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;

    }

    public User getUser1(){
        return user1;
    }

    public User getUser2(){
        return user2;
    }

    public void setUser1(User user1){
        this.user1 = user1;
    }

    public void setUser2(User user2){
        this.user2 = user2;
    }

    public Long getFriendID(){
        return friendID;
    }

    public void setFriendID(Long friendID){
        this.friendID = friendID;
    }


}
