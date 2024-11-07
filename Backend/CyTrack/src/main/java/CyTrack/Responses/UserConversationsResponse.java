package CyTrack.Responses;

import java.util.Date;
import java.util.List;

public class UserConversationsResponse {
    private String status;
    private Data data;
    private String message;

    public UserConversationsResponse(String status, List<UserConversationsData> userConversations, String message) {
        this.status = status;
        this.data = new Data(userConversations);
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
        private List<UserConversationsData> userConversations;

        public Data(List<UserConversationsData> userConversations) {
            this.userConversations = userConversations;
        }

        public List<UserConversationsData> getUserConversations() {
            return userConversations;
        }

        public void setUserConversations(List<UserConversationsData> userConversations) {
            this.userConversations = userConversations;
        }
    }

    public static class UserConversationsData {
        private String username;
        private String firstName;
        private String content;
        private String time;
        private Long userID;
        private Long friendEntityID;
        private Long conversationID;

        // Constructor, getters, and setters

        public UserConversationsData(String username, String firstName, String content, String time, Long userID, Long friendEntityID, Long conversationID) {
            this.username = username;
            this.firstName = firstName;
            this.content = content;
            this.time = time;
            this.userID = userID;
            this.friendEntityID = friendEntityID;
            this.conversationID = conversationID;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

        public Long getFriendEntityID() {
            return friendEntityID;
        }

        public void setFriendEntityID(Long friendEntityID) {
            this.friendEntityID = friendEntityID;
        }

        public Long getConversationID() {
            return conversationID;
        }

        public void setConversationID(Long conversationID) {
            this.conversationID = conversationID;
        }
    }

}
