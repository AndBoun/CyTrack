package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Controller
@ServerEndpoint("/leaderboard")
public class LeaderBoardSocket {

    private static UserService userService;

    @Autowired
    public LeaderboardSocket(UserService userService) {
        LeaderboardSocket.userService = userService;
    }

    private static Map<Session, Long> sessionUserMap = new Hashtable<>();
    private static Map<Long, Session> userSessionMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Store session and associate it with the user
        // Here, we could retrieve the user ID based on some authentication or session parameter.
        Long userID = getUserIDFromSession(session);  // Assume a method to extract user ID
        sessionUserMap.put(session, userID);
        userSessionMap.put(userID, session);

        // Send the initial leaderboard to the newly connected user
        sendLeaderboard(session);
    }

    @OnMessage
    public void onMessage(Session session, String messageText) throws IOException {
        // Here, messageText can be used to trigger an update to the leaderboard (if necessary)
        // For instance, a user updating their workout time
        updateLeaderboard();

        // Optionally, you could send a confirmation message back to the user
        session.getBasicRemote().sendText("Leaderboard updated.");
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

    // Method to send the current leaderboard to a session
    private void sendLeaderboard(Session session) throws IOException {
        List<User> leaderboard = userService.getAllUsersSortedByTotalTime();
        String leaderboardMessage = generateLeaderboardMessage(leaderboard);
        session.getBasicRemote().sendText(leaderboardMessage);
    }

    // Method to update the leaderboard (triggered when a user's totalTime changes)
    private void updateLeaderboard() throws IOException {
        List<User> leaderboard = userService.getAllUsersSortedByTotalTime();
        String leaderboardMessage = generateLeaderboardMessage(leaderboard);

        // Send the updated leaderboard to all connected users
        for (Session session : userSessionMap.values()) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(leaderboardMessage);
            }
        }
    }

    // Generate leaderboard message (a simple text format for this example)
    private String generateLeaderboardMessage(List<User> leaderboard) {
        StringBuilder sb = new StringBuilder("Leaderboard:\n");
        int rank = 1;
        for (User user : leaderboard) {
            sb.append(String.format("%d. %s - Total Time: %d\n", rank++, user.getUsername(), user.getTotalTime()));
        }
        return sb.toString();
    }
}
