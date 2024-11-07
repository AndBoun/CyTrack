package CyTrack.Responses;

public class ChatMessageResponse {
    private String status;
    private Data data;
    private String message;

    public ChatMessageResponse(String status, Data data, String message) {
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
        private Long userID;
        private String contentofmessage;

        public Data(Long userID, String contentofmessage) {
            this.userID = userID;
            this.contentofmessage = contentofmessage;
        }

        public Long getUserID() {
            return userID;
        }

        public void setUserID(Long userID) {
            this.userID = userID;
        }


        public String getContentofmessage() {
            return contentofmessage;
        }

        public void setContentofmessage(String contentofmessage) {
            this.contentofmessage = contentofmessage;
        }
    }
}
