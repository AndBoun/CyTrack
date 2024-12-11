package CyTrack.Sockets;

import CyTrack.Entities.User;
import CyTrack.Entities.Workout;
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
@ServerEndpoint("/workoutSocket/{displayerID}/{viewerID}")
public class WorkoutTrackingSocket {

    private static final Map<Session, Long> sessionViewerMap = new ConcurrentHashMap<>();
    private static final Map<Long, List<Session>> displayerSessionMap = new ConcurrentHashMap<>();
    private static final Map<Long, String> activeWorkoutMap = new ConcurrentHashMap<>(); // Tracks active workout names by displayer ID
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

        // Send initial workout state to the viewer
        sendWorkoutStateToViewer(session, displayerID);
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

    // Handles starting a workout
    public static void startWorkout(Long displayerID, String workoutName) {
        activeWorkoutMap.put(displayerID, workoutName);
        broadcastWorkoutUpdate(displayerID);
    }

    // Handles ending a workout
    public static void endWorkout(Long displayerID) {
        activeWorkoutMap.remove(displayerID);
        broadcastWorkoutUpdate(displayerID); // Notify viewers that the workout ended
    }

    // Broadcasts the workout state to all viewers of a specific displayer
    private static void broadcastWorkoutUpdate(Long displayerID) {
        List<Session> sessions = displayerSessionMap.get(displayerID);
        if (sessions != null) {
            sessions.forEach(session -> {
                try {
                    sendWorkoutStateToViewer(session, displayerID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // Sends the workout state of a displayer to a specific viewer
    private static void sendWorkoutStateToViewer(Session session, Long displayerID) throws IOException {
        String workoutName = activeWorkoutMap.get(displayerID);
        WorkoutState workoutState = new WorkoutState(displayerID, workoutName != null, workoutName);
        String workoutData = objectMapper.writeValueAsString(workoutState);
        session.getBasicRemote().sendText(workoutData);
    }

    // WorkoutState class to hold the workout state
    private static class WorkoutState {
        private final Long displayerID;
        private final boolean isActive;
        private final String workoutName;

        public WorkoutState(Long displayerID, boolean isActive, String workoutName) {
            this.displayerID = displayerID;
            this.isActive = isActive;
            this.workoutName = workoutName;
        }

        // Getters (for serialization)
        public Long getDisplayerID() { return displayerID; }
        public boolean isActive() { return isActive; }
        public String getWorkoutName() { return workoutName; }
    }
}
