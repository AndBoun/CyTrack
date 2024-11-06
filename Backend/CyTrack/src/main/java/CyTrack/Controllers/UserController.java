package CyTrack.Controllers;

import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // Register user
    @PostMapping("")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            if (userService.findByUserName(user.getUsername()).isPresent()) {
                ErrorResponse response = new ErrorResponse("error", 409, "Username already exists", "Username already exists");
                return ResponseEntity.status(409).body(response);
            }
            else {
                User registeredUser = userService.registerUser(user);
                LoginResponse response = new LoginResponse("success", registeredUser.getUserID(), "User registered" );
                return ResponseEntity.status(201).body(response);
            }
        } catch (NoSuchAlgorithmException e) {
            ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            Optional<User> foundUser = userService.findByUserName(user.getUsername());
            if (foundUser.isPresent() && userService.checkPassword(foundUser.get(), user.getPassword())) {
                LoginResponse response = new LoginResponse("success", foundUser.get().getUserID(), "Login successful");
                return ResponseEntity.status(201).body(response);
            } else {
                ErrorResponse response = new ErrorResponse("error", 401, "Account Does Not Exist", "Unauthorized");
                return ResponseEntity.status(401).body(response);
            }
        } catch (NoSuchAlgorithmException e) {
            ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Get all users
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userID}")
    public ResponseEntity<?> getUserByUserID(@PathVariable Long userID) {
        Optional<User> user = userService.findByUserID(userID);
        if (user.isPresent()) {
            User foundUser = user.get();
            LoginResponse response = new LoginResponse(
                    "success",
                    foundUser.getUserID(),
                    foundUser.getUsername(),
                    foundUser.getFirstName(),
                    foundUser.getLastName(),
                    foundUser.getAge(),
                    foundUser.getGender(),
                    foundUser.getStreak(),
                    "Resources sent successfully"
            );
            return ResponseEntity.status(201).body(response);
        } else {
            ErrorResponse error = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(error);
        }
    }

    // Update user information
    @PutMapping("/{userID}")
    public ResponseEntity<?> updateUser(@PathVariable Long userID, @RequestBody User updatedUser) {
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
            if (updatedUser.getAge() != 0) {
                existingUser.setAge(updatedUser.getAge());
            }
            if (updatedUser.getGender() != null) {
                existingUser.setGender(updatedUser.getGender());
            }
            User updated = userService.updateUser(existingUser);
            LoginResponse response = new LoginResponse("success", updated.getUserID(), "User updated");
            return ResponseEntity.ok(response);
        }
        else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }
    /*
    Post request check if the user exists and if the password matches the current password
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<?> SendUserIDForPassReset(@RequestBody User user) {
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
                ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Internal server error");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Resets password by userID
    @PutMapping("/resetPassword/{userID}")
    public ResponseEntity<?> resetPasswordByUserID(@PathVariable Long userID, @RequestBody User user) {
        Optional<User> foundUser = userService.findByUserID(userID);
        if (foundUser.isPresent()) {
            try {
                User updatedUser = userService.resetPassword(foundUser.get(), user.getPassword());
                passwordResponse response = new passwordResponse("success", "Password reset", updatedUser.getUserID());
                return ResponseEntity.ok(response);
            } catch (NoSuchAlgorithmException e) {
                ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "Internal server error");
                return ResponseEntity.status(500).body(response);
            }
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }
    // Delete user
    @DeleteMapping("/{userID}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userID) {
        Optional<User> foundUser = userService.findByUserID(userID);
        if (foundUser.isPresent()) {
            userService.deleteUser(foundUser.get());
            DeleteResponse response = new DeleteResponse("success", 200, "User deleted");
            return ResponseEntity.ok(response);
        } else {
            ErrorResponse response = new ErrorResponse("error", 404, "User not found", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    /*
    // Award a badge to a user -- IN PROGRESS
    @PostMapping("/{userId}/badges/{badgeId}")
    public ResponseEntity<?> awardBadge(@PathVariable Long userId, @PathVariable Long badgeId) {
        try {
            boolean badgeAwarded = userService.awardBadgeToUser(userId, badgeId);
            if (badgeAwarded) {
                return ResponseEntity.ok("Badge awarded successfully!");
            } else {
                ErrorResponse response = new ErrorResponse("error", 404, "User or Badge not found", "The user or badge with the specified ID does not exist.");
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse("error", 500, "Internal server error", "An unexpected error occurred.");
            return ResponseEntity.status(500).body(response);
        }
    }
     */
}