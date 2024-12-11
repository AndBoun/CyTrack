package CyTrack.Sockets;

import CyTrack.Services.WorkoutTrackingService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/workoutSocket/{displayerID}/{viewerID}")
@Controller
public class WorkoutTrackingSocket {

    private static WorkoutTrackingService workoutTrackingService;
    private static final ConcurrentHashMap<Long, Session> activeSessions = new ConcurrentHashMap<>();

    public static void setWorkoutTrackingService(WorkoutTrackingService service) {
        workoutTrackingService = service;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("displayerID") Long displayerID, @PathParam("viewerID") Long viewerID) {
        activeSessions.put(displayerID, session);
        try {
            workoutTrackingService.handleOpen(session, displayerID, viewerID);
            session.getBasicRemote().sendText("Connection established with displayerID: " + displayerID + " and viewerID: " + viewerID);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                session.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("displayerID") Long displayerID) {
        activeSessions.remove(displayerID);
        workoutTrackingService.handleClose(session, displayerID);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        workoutTrackingService.handleError(session, throwable);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        workoutTrackingService.handleMessage(message, session);
    }

    public void notifyWorkoutStarted(Long userID, Long workoutID) throws IOException {
        Session session = activeSessions.get(userID);
        if (session != null) {
            workoutTrackingService.startWorkout(userID, workoutID);
            session.getBasicRemote().sendText("Workout started for user: " + userID);

            // Broadcast timer updates
            new Thread(() -> {
                try {
                    int elapsedMinutes = 0;
                    while (activeSessions.containsKey(userID)) {
                        Thread.sleep(1000); // Wait for 1 second
                        elapsedMinutes++;
                        broadcastTimerUpdate(userID, elapsedMinutes);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void notifyWorkoutEnded(Long userID) throws IOException {
        Session session = activeSessions.get(userID);
        if (session != null) {
            workoutTrackingService.endWorkout(userID);
            session.getBasicRemote().sendText("Workout ended for user: " + userID);
            activeSessions.remove(userID);
        }
    }

    private void broadcastTimerUpdate(Long userID, int elapsedMinutes) throws IOException {
        String message = "Workout ongoing for user " + userID + ": " + elapsedMinutes + " minutes elapsed.";
        for (Session session : activeSessions.values()) {
            session.getBasicRemote().sendText(message);
        }
    }
}
