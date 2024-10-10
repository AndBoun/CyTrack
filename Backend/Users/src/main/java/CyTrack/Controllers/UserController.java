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
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Register user
    @PostMapping("")
    public ResponseEntity<LoginResponse> registerUser(@RequestBody User user) {
        LOGGER.log(Level.INFO, "Received registration request for user: {0}", user.getUsername());
        try {
            if (user.getUsername() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (userService.findByUserName(user.getUsername()).isPresent()) {
                return ResponseEntity.status(409).body(null);
            }
            else {
                User registeredUser = userService.registerUser(user);
                LOGGER.log(Level.INFO, "User registered with username: {0}", user.getUsername());
                LoginResponse response = new LoginResponse("success", "User registered", registeredUser.getUserID());
                return ResponseEntity.status(201).body(response);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error during user registration", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody User user) {
        try {
            Optional<User> foundUser = userService.findByUserName(user.getUsername());
            if (foundUser.isPresent() && userService.checkPassword(foundUser.get(), user.getPassword())) {
                LoginResponse response = new LoginResponse("success", "Login successful", foundUser.get().getUserID());
                return ResponseEntity.status(201).body(response);
            } else {
                LoginResponse response = new LoginResponse("error", "Invalid username or password", null);
                return ResponseEntity.status(401).body(response);
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Error during user login", e);
            LoginResponse response = new LoginResponse("error", "Internal server error", null);
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get all users
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by username
    /*
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUserName(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
     */

    @GetMapping("/{userID}")
    public ResponseEntity<LoginResponse> getUserByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            User foundUser = user.get();
            LoginResponse response = new LoginResponse(
                    "success",
                    "Resource created successfully",
                    foundUser.getUserID(),
                    foundUser.getFirstName(),
                    foundUser.getLastName(),
                    foundUser.getAge(),
                    foundUser.getGender(),
                    foundUser.getStreak()
            );
            return ResponseEntity.status(201).body(response);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Update user information
    @PutMapping("/{userID}")
    public ResponseEntity<User> updateUser(@PathVariable Long userID, @RequestBody User updatedUser) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            User existingUser = user.get();
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            if (updatedUser.getUsername() != null) {
                existingUser.setUsername(updatedUser.getUsername());
            }
            User updated = userService.updateUser(existingUser);
            return ResponseEntity.ok(updated);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<passwordResponse> SendUserIDForPassReset(@RequestBody User user) {
        Optional<User> foundUser = userService.findByUserName(user.getUsername());
        if (foundUser.isPresent()) {
            try {
                User existingUser = foundUser.get();
                if (!userService.checkPassword(existingUser, user.getPassword())) {
                    passwordResponse response = new passwordResponse("success", "Password does not match", existingUser.getUserID());
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(400).body(new passwordResponse("error", "New password cannot be the same as the current password", null));
                }
            } catch (NoSuchAlgorithmException e) {
                LOGGER.log(Level.SEVERE, "Error during password reset", e);
                return ResponseEntity.status(500).body(null);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody User user) {
        Optional<User> foundUser = userService.findByUserName(user.getUsername());
        if (foundUser.isPresent()) {
            try {
                User updatedUser = userService.resetPassword(foundUser.get(), user.getPassword());
                return ResponseEntity.ok("Password reset");
            } catch (NoSuchAlgorithmException e) {
                LOGGER.log(Level.SEVERE, "Error during password reset", e);
                return ResponseEntity.status(500).body("Internal server error");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteUser(@RequestBody User user) {
        Optional<User> foundUser = userService.findByUserName(user.getUsername());
        if (foundUser.isPresent()) {
            userService.deleteUser(foundUser.get());
            return ResponseEntity.ok("User deleted");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}