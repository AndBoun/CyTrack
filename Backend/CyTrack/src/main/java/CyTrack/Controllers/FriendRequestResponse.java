package CyTrack.Controllers;

import CyTrack.Entities.FriendRequest;

import java.util.List;

public class FriendRequestResponse {
    private String status;
    private Data data;
    private String message;

    public FriendRequestResponse(String status, List<FriendRequestData> friendRequest, String message) {
        this.status = status;
        this.data = new Data(friendRequest);
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
        private List<FriendRequestData> friendRequests;

        public Data(List<FriendRequestData> friendRequests) {
            this.friendRequests = friendRequests;
        }

        public List<FriendRequestData> getFriendRequests() {
            return friendRequests;
        }

        public void setFriendRequests(List<FriendRequestData> friendRequests) {
            this.friendRequests = friendRequests;
        }
    }

    public static class FriendRequestData {
        private String username;
        private String firstname;
        private Long id;

        public FriendRequestData(String username, String firstname, Long id) {
            this.username = username;
            this.firstname = firstname;
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }


    }

}
