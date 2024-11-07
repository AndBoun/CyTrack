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
@ServerEndpoint("/leaderboard/{userID}")
public class LeaderBoardSocket {

    private static final Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();
    private static UserService userService; // Static reference to UserService

    @Autowired
    public LeaderBoardSocket(UserService userService) {
        LeaderBoardSocket.userService = userService;
    }

    public LeaderBoardSocket(){}

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

        // Send the updated leaderboard on connection
        updateLeaderboard(userID);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        // You can handle dynamic leaderboard updates or messages here, if necessary
        // For example, this could be used for custom requests, if any.

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

    // Method to broadcast leaderboard to all connected users
    private static void broadcastLeaderboard(List<User> leaderboard) {
        sessionUserMap.keySet().forEach(session -> {
            try {
                String leaderboardData = generateLeaderboardData(leaderboard);
                session.getBasicRemote().sendText(leaderboardData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Generate leaderboard data as a JSON string
    private static String generateLeaderboardData(List<User> leaderboard) {
        StringBuilder leaderboardJson = new StringBuilder("[");
        for (User user : leaderboard) {
            leaderboardJson.append("{")
                    .append("\"userID\":").append(user.getUserID()).append(",")
                    .append("\"username\":\"").append(user.getUsername()).append("\",")
                    .append("\"totalTime\":").append(user.getTotalTime())
                    .append("},");
        }
        if (leaderboardJson.length() > 1) {
            leaderboardJson.deleteCharAt(leaderboardJson.length() - 1); // Remove last comma
        }
        leaderboardJson.append("]");
        return leaderboardJson.toString();
    }

    // Method to update the leaderboard (called from external sources, e.g., after a workout update)
    public static void updateLeaderboard(Long userID) {
        List<User> leaderboard = userService.getAllUsersSortedByTotalTimeDescending();
        broadcastLeaderboard(leaderboard);
    }
}
