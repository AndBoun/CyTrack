package CyTrack.Responses.Social;

import java.util.List;

public class GroupChatListResponse {
    private String status;
    private List<Data> data;
    private String message;

    public GroupChatListResponse(String status, List<Data> data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data {
        private Long groupID;
        private String groupName;
        private Long adminID;

        public Data(Long groupID, String groupName, Long adminID) {
            this.groupID = groupID;
            this.groupName = groupName;
            this.adminID = adminID;
        }

        public Long getGroupID() {
            return groupID;
        }

        public void setGroupID(Long groupID) {
            this.groupID = groupID;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public Long getAdminID() {
            return adminID;
        }

        public void setAdminID(Long adminID) {
            this.adminID = adminID;
        }
    }
}