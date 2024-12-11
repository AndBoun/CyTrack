package CyTrack.Responses.Social;

import java.util.List;

public class GroupChatMemberResponse {
    private String status;
    private Data data;
    private String message;

    public GroupChatMemberResponse(String status, Data data, String message) {
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
        private Long userID;
        private List<Member> members;


        public Data (Long groupID, String groupName, Long userID, List<Member> members) {
            this.groupID = groupID;
            this.groupName = groupName;
            this.userID = userID;
            this.members = members;
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

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

        public List<Member> getMembers() {
            return members;
        }

        public void setMembers(List<Member> members) {
            this.members = members;
        }

        public static class Member {
            public Long userID;
            public String username;

            public Member(Long userID, String username) {
                this.userID = userID;
                this.username = username;
            }

            public Long getUserID() {
                return userID;
            }

            public void setUserID(Long userID) {
                this.userID = userID;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }


        }

    }
}
