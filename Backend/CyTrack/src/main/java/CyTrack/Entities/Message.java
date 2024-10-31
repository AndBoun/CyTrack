package CyTrack.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
/*
 *@Author Kai Quach
 * Class for the message entity
 */
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store IDs instead of names for sender and receiver
    private Long senderID;
    private Long receiverID;
    private String senderName;
    private String receiverName;

    private String content;

    @CreationTimestamp // Automatically set timestamp at creation
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // Constructors
    public Message() {}

    public Message(Long senderID, Long receiverID, String senderName, String receiverName, String content) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.content = content;
        this.senderName = senderName;
        this.receiverName = receiverName;

    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(Long receiverID) {
        this.receiverID = receiverID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

}
