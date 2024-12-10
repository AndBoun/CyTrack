package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/workoutSocket/{displayerID}/{viewerID}")
public class WorkoutTrackingSocket {

    private static final Map<Session, Long> sessionViewerMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Session>> displayerSessionMap = new ConcurrentHashMap<>();
    private static final Map<Long, Instant> workoutStartTimeMap = new ConcurrentHashMap<>(); // Tracks displayer workout start times
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static UserService userService;

    @Autowired
    public WorkoutTrackingSocket(UserService userService) {
        WorkoutTrackingSocket.userService = userService;
    }

    public WorkoutTrackingSocket() {}

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

        // Send initial timer state to the viewer
        sendTimerToViewer(session, displayerID);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received from viewer: " + message);
        // Optional: Handle specific viewer messages if needed
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

    // Handles starting the workout timer
    public static void startWorkout(Long displayerID) {
        workoutStartTimeMap.put(displayerID, Instant.now());
        broadcastTimerUpdate(displayerID);
    }

    // Handles stopping the workout timer
    public static void endWorkout(Long displayerID) {
        workoutStartTimeMap.remove(displayerID);
        broadcastTimerUpdate(displayerID); // Notify viewers that the workout ended
    }

    // Broadcasts the timer state to all viewers of a specific displayer
    private static void broadcastTimerUpdate(Long displayerID) {
        List<Session> sessions = displayerSessionMap.get(displayerID);
        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    sendTimerToViewer(session, displayerID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Sends the timer state of a displayer to a specific viewer
    private static void sendTimerToViewer(Session session, Long displayerID) throws IOException {
        Instant startTime = workoutStartTimeMap.get(displayerID);
        TimerState timerState = new TimerState(displayerID, startTime != null, calculateElapsedTime(startTime));
        String timerData = objectMapper.writeValueAsString(timerState);
        session.getBasicRemote().sendText(timerData);
    }

    // Calculates elapsed time in seconds
    private static long calculateElapsedTime(Instant startTime) {
        if (startTime == null) return 0;
        return Instant.now().getEpochSecond() - startTime.getEpochSecond();
    }

    // TimerState class to hold the timer state
    private static class TimerState {
        private final Long displayerID;
        private final boolean isActive;
        private final long elapsedSeconds;

        public TimerState(Long displayerID, boolean isActive, long elapsedSeconds) {
            this.displayerID = displayerID;
            this.isActive = isActive;
            this.elapsedSeconds = elapsedSeconds;
        }

        // Getters (for serialization)
        public Long getDisplayerID() { return displayerID; }
        public boolean isActive() { return isActive; }
        public long getElapsedSeconds() { return elapsedSeconds; }
    }
}
