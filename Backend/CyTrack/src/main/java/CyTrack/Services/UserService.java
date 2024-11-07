package CyTrack.Services;

import CyTrack.Entities.User;
import CyTrack.Entities.UserConversations;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

//Service for User entity
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}