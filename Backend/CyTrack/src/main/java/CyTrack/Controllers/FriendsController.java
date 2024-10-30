package CyTrack.Controllers;

import CyTrack.Repositories.FriendsRepository;
import CyTrack.Entities.Friends;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;

@RestController("/{userID}/friends")
public class FriendsController {

    private final FriendsService friendsService;
    private final UserService userService;

    @Autowired
    public FriendsController(FriendsService friendsService, UserService userService) {
        this.friendsService = friendsService;
        this.userService = userService;
    }



}
