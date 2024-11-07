package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import CyTrack.Services.UserService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;

@Controller
@ServerEndpoint("/leaderboard")
public class LeaderBoardSocket {

    private static UserService userService;

    @Autowired
    public LeaderBoardSocket(UserService userService) {
        LeaderBoardSocket.userService = userService;
    }

    public LeaderBoardSocket() {
    }

    private static Map<Session, Long> sessionUserMap = new Hashtable<>();
    private static Map<Long, Session> userSessionMap = new Hashtable<>();
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // For example, retrieve the user ID here; this might come from an auth token.
        Long userID = retrieveUserID(session);
        String username = retrieveUsername(userID);

        if (userID != null && username != null) {
            sessionUserMap.put(session, userID);
            userSessionMap.put(userID, session);
            sessionUsernameMap.put(session, username);

            // Send the initial leaderboard to the connected user
            sendLeaderboard(session);
        } else {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.CANNOT_ACCEPT,
                    "Invalid session: User authentication failed"));
        }
    }

    @OnMessage
    public void onMessage(Session session, String messageText) throws IOException {
        updateLeaderboardStatic();
        session.getBasicRemote().sendText("Leaderboard updated.");
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        sessionUsernameMap.remove(session);

        if (userID != null) {
            userSessionMap.remove(userID);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        System.out.println("[onError] " + username + ": " + throwable.getMessage());
    }

    private void sendLeaderboard(Session session) throws IOException {
        List<User> leaderboard = userService.getAllUsersSortedByTotalTimeDescending();
        String leaderboardMessage = generateLeaderboardMessage(leaderboard);
        session.getBasicRemote().sendText(leaderboardMessage);
    }

    // Static method to allow other classes to trigger a leaderboard update
    public static void updateLeaderboardStatic() {
        try {
            List<User> leaderboard = userService.getAllUsersSortedByTotalTimeDescending();
            String leaderboardMessage = generateLeaderboardMessage(leaderboard);
            for (Session session : userSessionMap.values()) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(leaderboardMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Modify `updateLeaderboard` if needed and make `generateLeaderboardMessage` static
    private static String generateLeaderboardMessage(List<User> leaderboard) {
        StringBuilder sb = new StringBuilder("Leaderboard:\n");
        int rank = 1;
        for (User user : leaderboard) {
            sb.append(String.format("%d. %s - Total Time: %d\n", rank++, user.getUsername(), user.getTotalTime()));
        }
        return sb.toString();
    }

    private Long retrieveUserID(Session session) {
        // Implement user ID extraction logic here, for example, from session parameters or tokens
        return 123L; // Placeholder ID for testing
    }

    private String retrieveUsername(Long userID) {
        // Retrieve username from the userService based on userID
        return userService.findByUserID(userID).get().getUsername();
    }
}
