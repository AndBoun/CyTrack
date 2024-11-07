package CyTrack.Controllers;


import CyTrack.Entities.User;
import CyTrack.Entities.Friends;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{userID}/friends")
public class FriendsController {

    private final FriendsService friendsService;
    private final UserService userService;

    @Autowired
    public FriendsController(FriendsService friendsService, UserService userService) {
        this.friendsService = friendsService;
        this.userService = userService;

    }
    //displays all friends of a user
    @GetMapping("")
    public ResponseEntity<?> getFriends(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (!user.isPresent()) {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }

        List<Friends> friends = friendsService.getAllFriends(userID);
        List<FriendResponse.FriendsData> friendsDataList = friends.stream()
                .map(friend -> {
                    if (friend.getUser1().getUserID().equals(userID)) {
                        return new FriendResponse.FriendsData(
                                friend.getUser2().getFirstName(),
                                friend.getUser2().getUsername(),
                                friend.getUser2().getUserID(),
                                friend.getFriendID()
                        );
                    } else {
                        return new FriendResponse.FriendsData(
                                friend.getUser1().getFirstName(),
                                friend.getUser1().getUsername(),
                                friend.getUser1().getUserID(),
                                friend.getFriendID()
                        );
                    }
                })
                .collect(Collectors.toList());
        FriendResponse response = new FriendResponse("success", friendsDataList, "Friends found");
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{friendID}")
    public ResponseEntity<?> removeFriend(@PathVariable Long friendID) {
        Optional<Friends> friend = friendsService.findByFriendID(friendID);
        if (!friend.isPresent()) {
            ErrorResponse response = new ErrorResponse("error", 404, "Friend not found", "Friend not found");
            return ResponseEntity.status(404).body(response);
        }
        friendsService.removeFriend(friendID);
        FriendResponse response = new FriendResponse("success", null, "Friend removed");
        return ResponseEntity.status(200).body(response);
    }





}
