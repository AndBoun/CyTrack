package CyTrack.Sockets;

import CyTrack.Entities.Message;
import CyTrack.Entities.User;
import CyTrack.Entities.UserConversations;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Responses.UserConversationsResponse;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@ServerEndpoint("/conversations/{userID}")
public class UserConversationsSocket {

    private static FriendsService friendsService;
    private static UserService userService;
    private static MessageRepository messageRepository;

    @Autowired
    public UserConversationsSocket(FriendsService friendsService, UserService userService, MessageRepository messageRepository) {
        UserConversationsSocket.friendsService = friendsService;
        UserConversationsSocket.userService = userService;
        UserConversationsSocket.messageRepository = messageRepository;
    }

    public UserConversationsSocket() {
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") Long userID) throws IOException {
        Optional<User> user = userService.findByUserID(userID);
        if (!user.isPresent()) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User not found"));
            return;
        }

        List<UserConversations> conversations = friendsService.getUserConversations(userID);
        List<UserConversationsResponse.UserConversationsData> conversationData = conversations.stream().map(conversation -> {
            Long friendEntityID = conversation.getConversationID(); // Use the ID of the Friends entity
            Long friendUserID = conversation.getUser1().getUserID().equals(userID) ? conversation.getUser2().getUserID() : conversation.getUser1().getUserID();
            Optional<Message> latestMessage = messageRepository.findLatestMessage(userID, friendUserID);
            String content = latestMessage.map(Message::getContent).orElse("");
            String time = latestMessage.map(message -> new SimpleDateFormat("HH:mm:ss").format(message.getDate())).orElse(null);

            return new UserConversationsResponse.UserConversationsData(
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1_username() : conversation.getUser2_username(),
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1().getFirstName() : conversation.getUser2().getFirstName(),
                    content,
                    time,
                    userID,
                    friendEntityID, // Use the Friends entity ID here
                    conversation.getConversationID()
            );
        }).collect(Collectors.toList());

        UserConversationsResponse response = new UserConversationsResponse("success", conversationData, "User conversations retrieved successfully");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);
        session.getBasicRemote().sendText(jsonResponse);
    }

    @OnClose
    public void onClose() {

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }


}