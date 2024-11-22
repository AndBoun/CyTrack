package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/userSocket/{userID}")
public class UserSocket {

    private static final Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();
    private static UserService userService; // Static reference to UserService

    @Autowired
    public UserSocket(UserService userService) {
        UserSocket.userService = userService;
    }

    public UserSocket(){}

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

        // Send the list of all users on connection
        updateUserList();
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        // Handle incoming messages if needed, e.g., for custom user interactions
        System.out.println("Message received: " + message);
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

    // Broadcast the user list to all connected sessions
    private static void broadcastUserList(List<User> users) {
        sessionUserMap.keySet().forEach(session -> {
            try {
                String userListData = generateUserListData(users);
                session.getBasicRemote().sendText(userListData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Generate user list data as a JSON string
    private static String generateUserListData(List<User> users) {
        StringBuilder userListJson = new StringBuilder("[");
        for (User user : users) {
            userListJson.append("{")
                    .append("\"userID\":").append(user.getUserID()).append(",")
                    .append("\"username\":\"").append(user.getUsername()).append("\"")
                    .append("},");
        }
        if (userListJson.length() > 1) {
            userListJson.deleteCharAt(userListJson.length() - 1); // Remove last comma
        }
        userListJson.append("]");
        return userListJson.toString();
    }

    // Method to update the user list (called from external sources, if needed)
    public static void updateUserList() {
        List<User> users = userService.getAllUsers();
        broadcastUserList(users);
    }
}
