// src/main/java/CyTrack/Responses/Social/MessageResponse.java

package CyTrack.Responses.Social;

import java.time.LocalDateTime;

public class MessageResponse {
    private String status;
    private Data data;
    private String message;

    public MessageResponse(String status, Data data, String message) {
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
        private String chatType;
        private String senderUsername;
        private String receiverUsername;
        private String groupName;
        private String content;
        private String time;
        private Long groupOrReceiverID;
        private Long userID;
        private LocalDateTime timeStamp;

        public Data(String chatType, String senderUsername, String receiverUsername, String groupName, String content, String time, Long id, Long userID) {
            this.chatType = chatType;
            this.senderUsername = senderUsername;
            this.receiverUsername = receiverUsername;
            this.groupName = groupName;
            this.content = content;
            this.time = time;
            this.groupOrReceiverID = id;
            this.userID = userID;
            this.timeStamp = timeStamp;
        }

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }

        public String getReceiverUsername() {
            return receiverUsername;
        }

        public void setReceiverUsername(String receiverUsername) {
            this.receiverUsername = receiverUsername;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
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

        public Long getGroupOrReceiverID() {
            return groupOrReceiverID;
        }

        public void setGroupOrReceiverID(Long groupOrReceiverID) {
            this.groupOrReceiverID = groupOrReceiverID;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }

    }
}