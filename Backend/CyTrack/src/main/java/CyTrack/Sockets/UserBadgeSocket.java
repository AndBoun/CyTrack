package CyTrack.Sockets;

import CyTrack.Entities.Badge;
import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/badgeSocket/{displayerID}/{viewerID}")
public class UserBadgeSocket {

    private static Map<Session, Long> sessionViewerMap = new ConcurrentHashMap<>(); // Maps sessions to viewerID
    private static Map<Long, List<Session>> displayerSessionMap = new ConcurrentHashMap<>(); // Maps displayerID to list of viewer sessions
    private static UserService userService;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public UserBadgeSocket(UserService userService) {
        UserBadgeSocket.userService = userService;
    }

    public UserBadgeSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("displayerID") Long displayerID, @PathParam("viewerID") Long viewerID) throws IOException {
        Optional<User> displayerUser = userService.findByUserID(displayerID);
        if (displayerUser.isEmpty()) {
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "User not found"));
            return;
        }

        sessionViewerMap.put(session, viewerID);
        displayerSessionMap.computeIfAbsent(displayerID, k -> new ArrayList<>()).add(session);
        System.out.println("WebSocket opened for displayerID: " + displayerID + ", viewerID: " + viewerID);

        // Send initial badge list to the viewer
        sendBadgeListToViewer(session, displayerID);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received from viewer: " + message);
        // Optional: Handle specific messages from viewers here if needed
    }

    @OnClose
    public void onClose(Session session) {
        Long viewerID = sessionViewerMap.remove(session);
        displayerSessionMap.forEach((displayerID, sessions) -> {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                displayerSessionMap.remove(displayerID);
            }
        });
        System.out.println("WebSocket closed for viewerID: " + viewerID);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    // Broadcasts the badge list to all viewers of a specific displayer
    public static void updateBadgeList(Long displayerID) {
        List<Session> sessions = displayerSessionMap.get(displayerID);
        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    sendBadgeListToViewer(session, displayerID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Sends the badge list of a displayer to a specific viewer
    private static void sendBadgeListToViewer(Session session, Long displayerID) throws IOException {
        Optional<User> displayerUser = userService.findByUserIDWithBadges(displayerID);
        if (displayerUser.isPresent()) {
            List<Badge> badges = displayerUser.get().getBadges();
            String badgeData = objectMapper.writeValueAsString(badges);
            session.getBasicRemote().sendText(badgeData);
        }
    }
}
