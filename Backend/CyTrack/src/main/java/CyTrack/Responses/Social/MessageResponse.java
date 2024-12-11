package CyTrack.Responses.Social;

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
        private String chatType; // "group" or "direct"
        private String senderUsername;
        private String receiverUsername;
        private String content;
        private String time;
        private Long id;

        public Data(String chatType, String senderUsername, String receiverUsername, String content, String time, Long id) {
            this.chatType = chatType;
            this.senderUsername = senderUsername;
            this.receiverUsername = receiverUsername;
            this.content = content;
            this.time = time;
            this.id = id;
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}