package CyTrack.Controllers;

import CyTrack.Repositories.FriendsRepository;
import CyTrack.Entities.Friends;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController("/user/{userID}/friends")
public class FriendsController {

    @Autowired
    FriendsRepository friendsRepository;

    @Autowired
    UserRepository userRepository;

}
