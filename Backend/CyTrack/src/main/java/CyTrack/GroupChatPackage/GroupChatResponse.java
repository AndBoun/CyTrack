package CyTrack.GroupChatPackage;

public class GroupChatResponse {
    private String status;
    private Data data;
    private String message;

    public GroupChatResponse(String status, Data data, String message) {
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
