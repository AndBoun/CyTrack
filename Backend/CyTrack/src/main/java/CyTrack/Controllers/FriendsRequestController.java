package CyTrack.Controllers;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.User;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/{userID}/friendsRequest")
public class FriendsRequestController {
    private final UserService userService;
    private final FriendsService friendsService;
    @Autowired
    public FriendsRequestController(UserService userService, FriendsService friendsService) {
        this.userService = userService;
        this.friendsService = friendsService;
    }
    //Sends a friend request, by inputting a person's username.
    @PostMapping("")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long userID, @RequestBody Map<String, String> friendUser) {
        String friendUsername = friendUser.get("friendUsername");
        if (friendUsername == null || friendUsername.isEmpty()) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid username", "Username cannot be null or empty");
            return ResponseEntity.status(400).body(response);
        }

        Optional<User> friend = userService.findByUserName(friendUsername);
        if (friend.isPresent()) {
            if (friend.get().getUserID().equals(userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Invalid request", "You cannot send a friend request to yourself");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfFriends(userID,friend.get().getUserID())) {
                ErrorResponse response = new ErrorResponse("error", 400, "Already friends", "You are already friends with this user");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfRequestSent(userID, friend.get().getUserID())) {
                ErrorResponse response = new ErrorResponse("error", 400, "Request already sent", "You have already sent a friend request to this user");
                return ResponseEntity.status(400).body(response);
            }

            if (friendsService.checkIfRequestReceived(friend.get().getUserID(), userID)) {
                ErrorResponse response = new ErrorResponse("error", 400, "Request already received", "This user already has a pending friend request from you");
                return ResponseEntity.status(400).body(response);
            }

            FriendRequest friendRequest = friendsService.sendFriendRequest(friend.get().getUserID(), userID);
            FriendRequestIDResponse response = new FriendRequestIDResponse("success", friendRequest.getFriendRequestID(), "Friend request sent");

            friendsService.sendNotif(userID);

            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    @PutMapping("")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable Long userID, @RequestBody Map<String, Long> friendRequestID){
        Long requestID = friendRequestID.get("friendRequestID");
        if (requestID == null) {
            ErrorResponse response = new ErrorResponse("error", 400, "Invalid request ID", "Request ID cannot be null");
            return ResponseEntity.status(400).body(response);
        }

        Optional<FriendRequest> friendRequest = friendsService.findFriendRequestByID(requestID);

        if (friendRequest.isPresent()){
            FriendRequest friendRequest1 = friendRequest.get();

            if (!friendRequest1.getStatus().equals(FriendRequest.RequestStatus.PENDING) || !friendRequest1.getReceiver().getUserID().equals(userID)){
                ErrorResponse response = new ErrorResponse("error", 400, "Invalid request", "Friend request is not pending");
                return ResponseEntity.status(400).body(response);
            }

            friendsService.acceptFriendRequest(friendRequest1);

            FriendRequestIDResponse response = new FriendRequestIDResponse("success", requestID, "Friend request accepted");
            return ResponseEntity.status(200).body(response);


        }
        ErrorResponse response = new ErrorResponse("error", 404, "Friend request not found", "Friend request not found");
        return ResponseEntity.status(404).body(response);
    }




}
