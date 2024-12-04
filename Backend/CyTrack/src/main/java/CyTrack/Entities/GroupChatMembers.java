package CyTrack.Entities;

import jakarta.persistence.*;

@Entity
public class GroupChatMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupMessage groupChat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GroupChatMembers() {}

    public GroupChatMembers(GroupMessage groupChat, User user) {
        this.groupChat = groupChat;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupMessage getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupMessage groupChat) {
        this.groupChat = groupChat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
