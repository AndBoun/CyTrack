package CyTrack.Controllers;

// Friend request response specifically for friend request ID due to JSON formatting issues with friend request response
public class FriendRequestIDResponse {
    private String status;
    private Data data;
    private String message;

    public FriendRequestIDResponse(String status, Long friendRequestID, String message) {
        this.status = status; // Set the status
        this.data = new Data(friendRequestID); // Set the data with the friend request ID
        this.message = message; // Set the message
    }

    public String getStatus() { // Getter for status
        return status;
    }

    public void setStatus(String status) { // Setter for status
        this.status = status;
    }

    public Data getData() { // Getter for data
        return data;
    }

    public void setData(Data data) { // Setter for data
        this.data = data;
    }

    public String getMessage() { // Getter for message
        return message;
    }

    public void setMessage(String message) { // Setter for message
        this.message = message;
    }

    public static class Data {
        private Long friendRequestID; // Friend request ID

        public Data(Long friendRequestID) {
            this.friendRequestID = friendRequestID; // Set the friend request ID
        }

        public Long getFriendRequestID() { // Getter for friend request ID
            return friendRequestID;
        }

        public void setFriendRequestID(Long friendRequestID) { // Setter for friend request ID
            this.friendRequestID = friendRequestID;
        }
    }
}