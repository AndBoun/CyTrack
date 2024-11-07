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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
@ServerEndpoint("/conversations/{userID}")
public class UserConversationsSocket {

    private static FriendsService friendsService;
    private static UserService userService;
    private static MessageRepository messageRepository;
    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public UserConversationsSocket(FriendsService friendsService, UserService userService, MessageRepository messageRepository) {
        UserConversationsSocket.friendsService = friendsService;
        UserConversationsSocket.userService = userService;
        UserConversationsSocket.messageRepository = messageRepository;
    }

    public UserConversationsSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") Long userID) throws IOException {
        Optional<User> user = userService.findByUserID(userID);
        if (!user.isPresent()) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User not found"));
            return;
        }

        sessionUserMap.put(session, userID);
        userSessionMap.computeIfAbsent(userID, k -> new ArrayList<>()).add(session);

        System.out.println("WebSocket opened for userID: " + userID);

        // Load conversation data
        updateConversations(userID);
    }

    @OnMessage
    public void onMessage(Session session, String messageJson) throws IOException {
        Long senderID = sessionUserMap.get(session);
        if (senderID == null) return;  // Ignore if session is not mapped correctly

        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(messageJson, Message.class);

        // Save the message to the repository
        messageRepository.save(message);
        System.out.println("Message received from userID: " + senderID + " - " + messageJson);

        Long receiverID = message.getReceiver().getUserID();

        // Broadcast to all sessions of the sender and receiver
        broadcastMessage(senderID, messageJson);
        broadcastMessage(receiverID, messageJson);

        // Update the conversation list for both users
        updateConversations(senderID);
        updateConversations(receiverID);

        // Send updated conversation data to both users
        sendUpdatedConversations(senderID);
        sendUpdatedConversations(receiverID);
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            List<Session> sessions = userSessionMap.get(userID);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    userSessionMap.remove(userID);
                }
            }
        }
        System.out.println("WebSocket closed for userID: " + userID);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private void broadcastMessage(Long userID, String messageJson) throws IOException {
        List<Session> sessions = userSessionMap.get(userID);
        if (sessions != null) {
            for (Session userSession : sessions) {
                if (userSession.isOpen()) {
                    userSession.getBasicRemote().sendText(messageJson);
                }
            }
        }
    }

    private void updateConversations(Long userID) throws IOException {
        List<UserConversations> conversations = friendsService.getUserConversations(userID);
        List<UserConversationsResponse.UserConversationsData> conversationData = conversations.stream().map(conversation -> {
            Long friendEntityID = conversation.getConversationID();
            Long friendUserID = conversation.getUser1().getUserID().equals(userID) ? conversation.getUser2().getUserID() : conversation.getUser1().getUserID();
            List<Message> latestMessages = messageRepository.findLatestMessage(userID, friendUserID, PageRequest.of(0, 1));
            Message latestMessage = latestMessages.isEmpty() ? null : latestMessages.get(0);
            String content = latestMessage != null ? latestMessage.getContent() : "";
            String time = latestMessage != null ? new SimpleDateFormat("HH:mm:ss").format(latestMessage.getDate()) : null;

            return new UserConversationsResponse.UserConversationsData(
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1_username() : conversation.getUser2_username(),
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1().getFirstName() : conversation.getUser2().getFirstName(),
                    content,
                    time,
                    friendUserID,
                    friendEntityID,
                    conversation.getConversationID()
            );
        }).collect(Collectors.toList());

        UserConversationsResponse response = new UserConversationsResponse("success", conversationData, "User conversations updated");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);

        List<Session> sessions = userSessionMap.get(userID);
        if (sessions != null) {
            for (Session userSession : sessions) {
                if (userSession.isOpen()) {
                    userSession.getBasicRemote().sendText(jsonResponse);
                }
            }
        }
    }

    private void sendUpdatedConversations(Long userID) throws IOException {
        List<UserConversations> conversations = friendsService.getUserConversations(userID);
        List<UserConversationsResponse.UserConversationsData> conversationData = conversations.stream().map(conversation -> {
            Long friendEntityID = conversation.getConversationID();
            Long friendUserID = conversation.getUser1().getUserID().equals(userID) ? conversation.getUser2().getUserID() : conversation.getUser1().getUserID();
            List<Message> latestMessages = messageRepository.findLatestMessage(userID, friendUserID, PageRequest.of(0, 1));
            Message latestMessage = latestMessages.isEmpty() ? null : latestMessages.get(0);
            String content = latestMessage != null ? latestMessage.getContent() : "";
            String time = latestMessage != null ? new SimpleDateFormat("HH:mm:ss").format(latestMessage.getDate()) : null;

            return new UserConversationsResponse.UserConversationsData(
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1_username() : conversation.getUser2_username(),
                    friendUserID.equals(conversation.getUser1().getUserID()) ? conversation.getUser1().getFirstName() : conversation.getUser2().getFirstName(),
                    content,
                    time,
                    friendUserID,
                    friendEntityID,
                    conversation.getConversationID()
            );
        }).collect(Collectors.toList());

        UserConversationsResponse response = new UserConversationsResponse("success", conversationData, "User conversations updated");
        String jsonResponse = new ObjectMapper().writeValueAsString(response);

        List<Session> sessions = userSessionMap.get(userID);
        if (sessions != null) {
            for (Session userSession : sessions) {
                if (userSession.isOpen()) {
                    userSession.getBasicRemote().sendText(jsonResponse);
                }
            }
        }
    }
}