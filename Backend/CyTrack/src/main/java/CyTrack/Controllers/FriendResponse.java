package CyTrack.Controllers;

import java.util.List;

public class FriendResponse {
    private String status;
    private Data data;
    private String message;

    public FriendResponse(String status, List<FriendsData> friends, String message) {
        this.status = status;
        this.data = new Data(friends);
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
        private List<FriendsData> friends;

        public Data(List<FriendsData> friends) {
            this.friends = friends;
        }

        public List<FriendsData> getFriends() {
            return friends;
        }

        public void setFriends(List<FriendsData> friends) {
            this.friends = friends;
        }
    }

    public static class FriendsData {
        private String username;

        public FriendsData(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}