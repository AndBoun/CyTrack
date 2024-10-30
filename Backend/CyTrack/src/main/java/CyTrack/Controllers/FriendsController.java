package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Repositories.FriendsRepository;
import CyTrack.Entities.Friends;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;

import java.util.List;
import java.util.Optional;

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
                .map(friend -> new FriendResponse.FriendsData(
                        friend.getUser2().getUsername()
                ))
                .toList();
        FriendResponse response = new FriendResponse("success", friendsDataList, "Friends found");
        return ResponseEntity.status(200).body(response);
    }



}
