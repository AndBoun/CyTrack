package CyTrack.Sockets;

import CyTrack.Entities.Message;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Responses.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

@Controller
@ServerEndpoint("/chat/{senderID}/{receiverID}")
public class MessageSocket {

    private static MessageRepository msgRepo;
    private static FriendsService friendsService;
    private static UserService userService;


    @Autowired
    public MessageSocket(MessageRepository msgRepo, FriendsService friendsService, UserService userService) {
        MessageSocket.msgRepo = msgRepo;
        MessageSocket.friendsService = friendsService;
        MessageSocket.userService = userService;
    }

    public MessageSocket() {
    }

    private static Map<Session, Long> sessionUserMap = new Hashtable<>();
    private static Map<Long, Session> userSessionMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("senderID") Long senderID, @PathParam("receiverID") Long receiverID) throws IOException {
        // Check if the sender and receiver are friends
        if (friendsService.checkIfFriends(senderID, receiverID)) {
            sessionUserMap.put(session, senderID);
            userSessionMap.put(senderID, session);

            // Send chat history to the user when they connect
            sendChatHistory(session, senderID, receiverID);
        } else {
            // Close session if users are not friends
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    "You are not friends with this user"));
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("receiverID") Long receiverID, String messageText) throws IOException {
        Long senderID = sessionUserMap.get(session);
        if (senderID == null) return;  // Ignore if session is not mapped correctly

        // Check if the sender and receiver are friends
        if (friendsService.checkIfFriends(senderID, receiverID)) {
            // Create and save message
            String senderUsername = userService.findByUserID(senderID).get().getUsername();
            String receiverUsername = userService.findByUserID(receiverID).get().getUsername();
            Message message = new Message(senderID, receiverID, senderUsername, receiverUsername, messageText);
            msgRepo.save(message);

            Session receiverSession = userSessionMap.get(receiverID);
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.getBasicRemote().sendText( senderUsername + ": " + messageText);
            }

            session.getBasicRemote().sendText("You: " + messageText);
        } else {
            session.getBasicRemote().sendText("Error: You are not friends with this user");
        }
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            userSessionMap.remove(userID);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private void sendChatHistory(Session session, Long senderID, Long receiverID) throws IOException {
        var chatHistory = msgRepo.findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(senderID, receiverID);
        for (Message msg : chatHistory) {
            ChatMessageResponse chatMessage = new ChatMessageResponse("success",
                    new ChatMessageResponse.Data(msg.getSenderID(), msg.getContent()),
                    "Chat history loaded");
            String jsonMessage = new ObjectMapper().writeValueAsString(chatMessage);
            session.getBasicRemote().sendText(jsonMessage);
        }
    }
}