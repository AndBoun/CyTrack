package CyTrack.Sockets;

import CyTrack.Services.WorkoutTrackingService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@ServerEndpoint("/workoutSocket/{displayerID}/{viewerID}")
@Controller
public class WorkoutTrackingSocket {

    private static WorkoutTrackingService workoutTrackingService;

    public static void setWorkoutTrackingService(WorkoutTrackingService service) {
        workoutTrackingService = service;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("displayerID") Long displayerID, @PathParam("viewerID") Long viewerID) {
        workoutTrackingService.handleOpen(session, displayerID, viewerID);
    }

    @OnClose
    public void onClose(Session session, @PathParam("displayerID") Long displayerID) {
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
        workoutTrackingService.startWorkout(userID, workoutID);
    }

    public void notifyWorkoutEnded(Long userID) throws IOException {
        workoutTrackingService.endWorkout(userID);
    }
}
