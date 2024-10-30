package CyTrack.Services;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.Friends;
import CyTrack.Entities.User;
import CyTrack.Repositories.FriendRequestRepository;
import CyTrack.Repositories.FriendsRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    public FriendsService(FriendsRepository friendsRepository, FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendsRepository = friendsRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;

    }

    // Checks if two users are friends by seeing if there is an accepted friend request between them
    public boolean checkIfFriends(Long userID, Long friendID) {
        return friendsRepository.existsByUser1_UserIDAndUser2_UserID(userID, friendID) ||
                friendsRepository.existsByUser1_UserIDAndUser2_UserID(friendID, userID);
    }

    // Sends a friend request if one does not already exist in any status
    public FriendRequest sendFriendRequest(Long friendID, Long userID) {
        User sender = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(friendID).orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check if a friend request already exists in either direction
        if (checkIfRequestSent(userID, friendID) || checkIfRequestReceived(userID, friendID)) {
            throw new RuntimeException("Friend request already exists between these users");
        }

        // Create and save a new friend request with PENDING status
        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequest.setStatus(FriendRequest.RequestStatus.PENDING);
        return friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(FriendRequest friendRequest){
        // Update the friend request status to accepted
        friendRequest.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        // Create a new Friends record
        Friends friends = new Friends(friendRequest.getSender(), friendRequest.getReceiver());
        friends.setFriendRequest(friendRequest);
        friendsRepository.save(friends);
    }

    public void sendNotif(Long userId){
        String message = "You have a new friend request by " + userRepository.findById(userId).get().getUsername();
        template.convertAndSend("/topic/notification/", message);
    }

    // Checks if a friend request has already been sent from sender to receiver
    public boolean checkIfRequestSent(Long senderID, Long receiverID) {
        return friendRequestRepository.existsBySenderUserIDAndReceiverUserIDAndStatus(senderID, receiverID, FriendRequest.RequestStatus.PENDING);
    }

    // Checks if a friend request has already been received by the user from the friend
    public boolean checkIfRequestReceived(Long friendID, Long userID) {
        return friendRequestRepository.existsBySenderUserIDAndReceiverUserIDAndStatus(friendID, userID, FriendRequest.RequestStatus.PENDING);
    }

    public Optional<FriendRequest> findFriendRequestByID(Long requestID) {
        return friendRequestRepository.findById(requestID);
    }
}