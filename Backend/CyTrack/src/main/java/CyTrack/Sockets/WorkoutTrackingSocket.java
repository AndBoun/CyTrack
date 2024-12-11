package CyTrack.Sockets;

import CyTrack.Entities.Workout;
import CyTrack.Services.WorkoutTrackingService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/workoutSocket/{displayerID}/{viewerID}")
@Controller
public class WorkoutTrackingSocket {

    private static WorkoutTrackingService workoutTrackingService;

    private static final ConcurrentHashMap<Long, Session> userSessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, List<Session>> viewerSessions = new ConcurrentHashMap<>();

    public static void setWorkoutTrackingService(WorkoutTrackingService service) {
        workoutTrackingService = service;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("displayerID") Long displayerID, @PathParam("viewerID") Long viewerID) {
        if (viewerID != null) {
            viewerSessions.computeIfAbsent(displayerID, k -> new CopyOnWriteArrayList<>()).add(session);
        } else {
            userSessions.put(displayerID, session);
        }
        workoutTrackingService.handleOpen(session, displayerID, viewerID);
    }

    @OnClose
    public void onClose(Session session, @PathParam("displayerID") Long displayerID) {
        userSessions.remove(displayerID);
        viewerSessions.remove(displayerID);
        workoutTrackingService.handleClose(session, displayerID);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        workoutTrackingService.handleError(session, throwable);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        workoutTrackingService.handleMessage(message, session);
    }

    public void notifyWorkoutStarted(Long userID, Long workoutID) throws IOException {
        Workout workout = workoutTrackingService.findWorkoutByID(workoutID); // Updated
        if (workout != null) {
            String message = createWorkoutNotification(workout, "started");
            broadcastToUserAndViewers(userID, message);
        }
    }

    public void notifyWorkoutEnded(Long userID, Long workoutID) throws IOException {
        Workout workout = workoutTrackingService.findWorkoutByID(workoutID); // Updated
        if (workout != null) {
            String message = createWorkoutNotification(workout, "ended");
            broadcastToUserAndViewers(userID, message);
        }
    }

    private String createWorkoutNotification(Workout workout, String status) {
        long elapsedTime = Duration.between(workout.getStartTime(), workout.getEndTime() != null ? workout.getEndTime() : LocalDateTime.now()).getSeconds();
        return String.format("{\"userID\":%d,\"workoutID\":%d,\"status\":\"%s\",\"elapsedTime\":%d}",
                workout.getUser().getUserID(), workout.getWorkoutID(), status, elapsedTime);
    }

    private void broadcastToUserAndViewers(Long userID, String message) throws IOException {
        // Notify the user
        Session userSession = userSessions.get(userID);
        if (userSession != null && userSession.isOpen()) {
            userSession.getBasicRemote().sendText(message);
        }

        // Notify viewers
        List<Session> viewerSessions = getViewerSessions(userID);
        for (Session session : viewerSessions) {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        }
    }

    private List<Session> getViewerSessions(Long userID) {
        return viewerSessions.getOrDefault(userID, List.of());
    }
}
