package CyTrack.Services;

import CyTrack.Entities.User;
import CyTrack.Repositories.BadgeRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//Service for User entity
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;

    @Autowired
    public UserService(UserRepository userRepository, BadgeRepository badgeRepository) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
    }
    // Register user, encrypt password
    public User registerUser(User user) throws NoSuchAlgorithmException {
        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }
    //When user resets password, encrypt new password
    public User resetPassword(User user, String newPassword) throws NoSuchAlgorithmException {
        user.setPassword(hashPassword(newPassword));
        return userRepository.save(user);
    }



    public Optional<User> findByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User> findByUserID(Long userID){ return userRepository.findByUserID(userID);}

    // Method to fetch User with badges
    public Optional<User> findByUserIDWithBadges(Long userID) {
        return userRepository.findByUserIDWithBadges(userID);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    //Check if password is correct
    public boolean checkPassword(User user, String rawPassword) throws NoSuchAlgorithmException {
        return user.getPassword().equals(hashPassword(rawPassword));
    }
    private String hashPassword(String password) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
    }


    public List<User> getAllUsersSortedByTotalTimeDescending() {
        List<User> allUsers = userRepository.findAll();

        // Sort users by totalTime in descending order and return the sorted list
        return allUsers.stream()
                .sorted((u1, u2) -> Long.compare(u2.getTotalTime(), u1.getTotalTime())) // Descending order
                .collect(Collectors.toList());
    }
}