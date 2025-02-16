package CyTrack.Services.Social;

import CyTrack.Entities.*;
import CyTrack.Repositories.FriendRequestRepository;
import CyTrack.Repositories.FriendsRepository;
import CyTrack.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.Repositories.UserConversationsRepository;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;
    private final UserConversationsRepository UserConversationsRepository;
//    @Autowired
//    private SimpMessagingTemplate template;
    @Autowired
    public FriendsService(FriendsRepository friendsRepository, FriendRequestRepository friendRequestRepository, UserRepository userRepository, UserConversationsRepository UserConversationsRepository) {
        this.friendsRepository = friendsRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
        this.UserConversationsRepository = UserConversationsRepository;

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

        if (checkIfRequestSent(userID, friendID) || checkIfRequestReceived(userID, friendID)) {
            throw new RuntimeException("Friend request already exists between these users");
        }
        String senderUsername = sender.getUsername();
        String receiverUsername = receiver.getUsername();
        FriendRequest friendRequest = new FriendRequest(sender, senderUsername, receiver, receiverUsername);
        friendRequest.setStatus(FriendRequest.RequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);

//        sendNotification(receiver.getUserID(), sender.getUsername());

        return friendRequest;
    }

    public void acceptFriendRequest(FriendRequest friendRequest){
        // Update the friend request status to accepted
        friendRequest.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(friendRequest);

        // Create a new Friends record
        Friends friends = new Friends(friendRequest.getSender(), friendRequest.getSender().getUsername(), friendRequest.getReceiver(), friendRequest.getReceiver().getUsername());
        friends.setFriendRequest(friendRequest);
        friendsRepository.save(friends);

        // Create a new UserConversations record
        UserConversations userConversations = new UserConversations(friendRequest.getSender(), friendRequest.getSender().getUsername(), friendRequest.getReceiver(), friendRequest.getReceiver().getUsername());
        UserConversationsRepository.save(userConversations);
    }

    public void declineFriendRequest(FriendRequest friendRequest){
        // Update the friend request status to declined
        friendRequest.setStatus(FriendRequest.RequestStatus.DECLINED);
        friendRequestRepository.save(friendRequest);
    }
//    private void sendNotification(Long receiverId, String senderUsername) {
//        String message = "You have a new friend request from " + senderUsername;
//        template.convertAndSendToUser(receiverId.toString(), "/queue/friendRequest", message);
//    }

    public List<Friends> getAllFriends(Long userID) {
        return friendsRepository.findByUser1_UserIDOrUser2_UserID(userID, userID);
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

    public List<FriendRequest> getIncomingFriendRequests(Long userID) {
        return friendRequestRepository.findByReceiver_UserIDAndStatus(userID, FriendRequest.RequestStatus.PENDING);
    }

    public List<FriendRequest> getOutgoingFriendRequests(Long userID) {
        return friendRequestRepository.findBySender_UserIDAndStatus(userID, FriendRequest.RequestStatus.PENDING);
    }

    public Optional<Friends> findByFriendID(Long friendID) {
        return friendsRepository.findById(friendID);
    }

    public void removeFriend(Long friendID) {
        friendsRepository.deleteById(friendID);

    }
    public List<UserConversations> getUserConversations(Long userID) {
        return UserConversationsRepository.findByUser1_UserIDOrUser2_UserID(userID, userID);
    }


}