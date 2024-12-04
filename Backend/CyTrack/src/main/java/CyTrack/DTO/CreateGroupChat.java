package CyTrack.DTO;

import java.util.List;

public class CreateGroupChat {
    private Long adminID; // ID of the user creating the group
    private String groupName; // Name of the group chat
    private List<Long> members; // IDs of the initial members to add to the group

    public CreateGroupChat() {}

    public CreateGroupChat(Long adminID, String groupName, List<Long> members) {
        this.adminID = adminID;
        this.groupName = groupName;
        this.members = members;
    }

    public Long getAdminID() {
        return adminID;
    }

    public void setAdminID(Long adminID) {
        this.adminID = adminID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Long> getMembers() {
        return members;
    }

    public void setMembers(List<Long> members) {
        this.members = members;
    }
}
