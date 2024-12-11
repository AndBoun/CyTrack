package CyTrack.Sockets;

import CyTrack.Entities.Message;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Services.Social.FriendsService;
import CyTrack.Services.NotificationService;
import CyTrack.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/message/{senderID}/{receiverID}")
public class MessageSocket {

    private static MessageRepository msgRepo;
    private static FriendsService friendsService;
    private static UserService userService;
    private static NotificationService notificationService;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public MessageSocket(MessageRepository msgRepo, FriendsService friendsService, UserService userService, NotificationService notificationService) {
        MessageSocket.msgRepo = msgRepo;
        MessageSocket.friendsService = friendsService;
        MessageSocket.userService = userService;
        MessageSocket.notificationService = notificationService;
    }

    public MessageSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("senderID") Long senderID, @PathParam("receiverID") Long receiverID) throws IOException {
        System.out.println("Attempting to open WebSocket for senderID: " + senderID + " and receiverID: " + receiverID);
        boolean areFriends = friendsService.checkIfFriends(senderID, receiverID);
        System.out.println("Are friends: " + areFriends);

        if (areFriends) {
            sessionUserMap.put(session, senderID);
            userSessionMap.computeIfAbsent(senderID, k -> new ArrayList<>()).add(session);

            // Load chat history from the database
            List<Message> messageHistory = msgRepo.findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(senderID, receiverID);

            for (Message message : messageHistory) {
                String time = new SimpleDateFormat("HH:mm:ss").format(message.getDate());
                MessageResponse response = new MessageResponse(
                        "success",
                        new MessageResponse.Data(message.getSender().getUsername(), message.getReceiver().getUsername(), message.getContent(), time),
                        "Chat history loaded"
                );
                String jsonResponse = objectMapper.writeValueAsString(response);
                session.getBasicRemote().sendText(jsonResponse);
            }
        } else {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "You are not friends with this user"));
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("receiverID") Long receiverID, String messageText) throws IOException {
        Long senderID = sessionUserMap.get(session);
        if (senderID == null) return;

        if (friendsService.checkIfFriends(senderID, receiverID)) {
            String senderUsername = userService.findByUserID(senderID).get().getUsername();
            String receiverUsername = userService.findByUserID(receiverID).get().getUsername();
            Message message = new Message(senderID, receiverID, senderUsername, receiverUsername, messageText);
            msgRepo.save(message);

            String time = new SimpleDateFormat("HH:mm:ss").format(message.getDate());
            MessageResponse messageResponse = new MessageResponse("success", new MessageResponse.Data(senderUsername, receiverUsername, messageText, time), "New message received");
            String jsonMessage = objectMapper.writeValueAsString(messageResponse);

            // Send full message to receiver
            List<Session> receiverSessions = userSessionMap.get(receiverID);
            if (receiverSessions != null) {
                for (Session receiverSession : receiverSessions) {
                    if (receiverSession.isOpen()) {
                        System.out.println("Sending message to receiver: " + receiverID);
                        receiverSession.getBasicRemote().sendText(jsonMessage);
                    }
                }
            } else {
                // Notify offline users
                System.out.println("Receiver is offline, sending notification to receiver: " + receiverID);
                notificationService.notifyUser(receiverID, jsonMessage);
            }

            // Send "You: {message}" to sender
            session.getBasicRemote().sendText("You: " + messageText);

            // Broadcast update to central WebSocket
            System.out.println("Broadcasting update to central WebSocket for receiver: " + receiverID);
            NotificationsWebSocket.broadcastUpdate(receiverID, jsonMessage);
        } else {
            session.getBasicRemote().sendText("Error: You are not friends with this user");
        }
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
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    private static class MessageResponse {
        private String status;
        private Data data;
        private String message;

        public MessageResponse(String status, Data data, String message) {
            this.status = status;
            this.data = data;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static class Data {
            private String senderUsername;
            private String receiverUsername;
            private String content;
            private String time;

            public Data(String senderUsername, String receiverUsername, String content, String time) {
                this.senderUsername = senderUsername;
                this.receiverUsername = receiverUsername;
                this.content = content;
                this.time = time;
            }

            public String getSenderUsername() {
                return senderUsername;
            }

            public void setSenderUsername(String senderUsername) {
                this.senderUsername = senderUsername;
            }

            public String getReceiverUsername() {
                return receiverUsername;
            }

            public void setReceiverUsername(String receiverUsername) {
                this.receiverUsername = receiverUsername;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}