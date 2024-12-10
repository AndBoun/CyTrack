package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Services.FriendsService;
import CyTrack.Services.UserService;
import CyTrack.Repositories.MessageRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/notifications/{userID}")
public class NotificationsWebSocket {

    private static FriendsService friendsService;
    private static UserService userService;
    private static MessageRepository messageRepository;
    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public NotificationsWebSocket(FriendsService friendsService, UserService userService, MessageRepository messageRepository) {
        NotificationsWebSocket.friendsService = friendsService;
        NotificationsWebSocket.userService = userService;
        NotificationsWebSocket.messageRepository = messageRepository;
    }

    public NotificationsWebSocket() {}

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
    }

    @OnMessage
    public void onMessage(Session session, String messageJson) throws IOException {
        // Handle incoming messages if needed
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

    public static void broadcastUpdate(Long userID, String updateJson) throws IOException {
        System.out.println("Broadcasting update to userID: " + userID);
        List<Session> sessions = userSessionMap.get(userID);
        if (sessions != null) {
            for (Session userSession : sessions) {
                if (userSession.isOpen()) {
                    System.out.println("Sending update to session: " + userSession.getId());
                    userSession.getBasicRemote().sendText(updateJson);
                }
            }
        }
    }
}