package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        LOGGER.log(Level.INFO, "Received registration request for user: {0}", user.getUsername());
        try {
            if (user.getUsername() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            User registeredUser = userService.registerUser(user);
            LOGGER.log(Level.INFO, "User registered with username: {0}", user.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error during user registration", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            Optional<User> foundUser = userService.findByUserName(user.getUsername());
            if (foundUser.isPresent() && userService.checkPassword(foundUser.get(), user.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.status(401).body("Invalid username or password");
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error during user login", e);
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUserName(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUserName(username);
        if (user.isPresent()) {
            userService.deleteUser(user.get());
            return ResponseEntity.ok("User deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}