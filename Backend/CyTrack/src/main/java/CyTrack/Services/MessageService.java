package CyTrack.Services;

import CyTrack.Entities.UserConversations;
import CyTrack.Entities.Message;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Entities.Friends;
import CyTrack.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final FriendsService friendsService;

    public MessageService(MessageRepository messageRepository, FriendsService friendsService) {
        this.messageRepository = messageRepository;
        this.friendsService = friendsService;
    }

    public Message getMostRecentMessage(Long user1ID, Long user2ID) {
        List<Message> messages = messageRepository.findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(user1ID, user2ID, user2ID, user1ID);
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

}
