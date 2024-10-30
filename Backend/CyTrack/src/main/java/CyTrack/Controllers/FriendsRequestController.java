package CyTrack.Controllers;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;
import CyTrack.Services.WorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long userID, @RequestParam String friendUsername) {
        Optional<User> friend = userService.findByUserName(friendUsername);
        if (friend.isPresent()){
            //checks if the user is already friends with the friend
            if (friendsService.checkIfFriends(friend.get().getUserID())){
                ErrorResponse response = new ErrorResponse("error", 400, "Already friends", "You are already friends with this user");
                return ResponseEntity.status(400).body(response);
            }

            //checks if the user has already sent a friend request to the friend
            if (friendsService.checkIfRequestSent(friend.get().getUserID())){
                ErrorResponse response = new ErrorResponse("error", 400, "Request already sent", "You have already sent a friend request to this user");
                return ResponseEntity.status(400).body(response);
            }

            //sends the friend request
            FriendRequest friendRequest = friendsService.sendFriendRequest(friend.get().getUserID(), userID);
            FriendRequestIDResponse response = new FriendRequestIDResponse("success", friendRequest.getFriendRequestID(), "Friend request sent");
            return ResponseEntity.status(201).body(response);
        }
        ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
        return ResponseEntity.status(404).body(response);
    }





}
