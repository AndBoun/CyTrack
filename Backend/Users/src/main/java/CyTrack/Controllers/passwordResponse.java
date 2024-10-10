package CyTrack.Controllers;

public class passwordResponse {
    private String status;
    private String message;
    private Data data;

    public passwordResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public passwordResponse(String status, String message, Long userID) {
        this.status = status;
        this.message = message;
        this.data = new Data(userID);
    }
    public String getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
    public Data getData() {
        return data;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setData(Data data) {
        this.data = data;
    }



    public static class Data {
        private String password;
        private Long userID;

        public Data (Long userID) {
            this.userID = userID;
            this.password = password;
        }


        public Long getUserID() {
            return userID;
        }
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
