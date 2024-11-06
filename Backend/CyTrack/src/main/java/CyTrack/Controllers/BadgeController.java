package CyTrack.Controllers;

import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/badge")
public class BadgeController {

    private final UserService userService;

    @Autowired
    public BadgeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * LIST ALL BADGES for a SPECIFIC USER
     */

}
