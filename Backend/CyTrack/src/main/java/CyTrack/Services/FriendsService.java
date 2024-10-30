package CyTrack.Services;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.User;
import CyTrack.Repositories.FriendsRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendsService(FriendsRepository friendsRepository, UserRepository userRepository) {
        this.friendsRepository = friendsRepository;
        this.userRepository = userRepository;
    }

    public boolean checkIfFriends(Long userID) {
        return friendsRepository.existsBySenderUserID(userID) || friendsRepository.existsByReceiverUserID(userID);
    }

    public FriendRequest sendFriendRequest(Long friendID, Long userID) {
        User sender = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(friendID).orElseThrow(() -> new RuntimeException("Receiver not found"));
        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        return friendsRepository.save(friendRequest);
    }

    public boolean checkIfRequestSent(Long friendID) {
        return friendsRepository.existsByReceiverUserID(friendID);
    }
}