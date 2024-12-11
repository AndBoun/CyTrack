package CyTrack.Services;

import CyTrack.Entities.Workout;
import CyTrack.Entities.User;
import jakarta.websocket.Session;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkoutTrackingService {

    private static final ConcurrentHashMap<Long, Session> userSessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Workout> activeWorkouts = new ConcurrentHashMap<>();

    private final WorkoutService workoutService;
    private final UserService userService;

    public WorkoutTrackingService(WorkoutService workoutService, UserService userService) {
        this.workoutService = workoutService;
        this.userService = userService;
    }

    public void handleOpen(Session session, Long displayerID, Long viewerID) {
        userSessions.put(viewerID, session);
        sendWorkoutStatus(session, displayerID);
    }

    public void handleClose(Session session, Long displayerID) {
        userSessions.remove(displayerID);
        activeWorkouts.remove(displayerID);
    }

    public void handleError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    public void handleMessage(String message, Session session) {
        // Handle any specific commands sent from the client if necessary
    }

    public void startWorkout(Long userID, Long workoutID) {
        Workout workout = workoutService.findByWorkoutID(workoutID).orElse(null);
        if (workout != null) {
            activeWorkouts.put(userID, workout);
            broadcastWorkoutStatus(userID);
        }
    }

    public void endWorkout(Long userID) {
        activeWorkouts.remove(userID);
        broadcastWorkoutStatus(userID);
    }

    private void sendWorkoutStatus(Session session, Long displayerID) {
        Workout workout = activeWorkouts.get(displayerID);
        if (workout != null) {
            long elapsedSeconds = 0;
            boolean active = workout.getEndTime() == null;

            if (active && workout.getStartTime() != null) {
                elapsedSeconds = Duration.between(workout.getStartTime(), LocalDateTime.now()).getSeconds();
            }

            String status = String.format(
                    "{\"displayerID\":%d,\"elapsedSeconds\":%d,\"active\":%b}",
                    workout.getWorkoutID(), elapsedSeconds, active
            );

            try {
                session.getBasicRemote().sendText(status);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendNoActiveWorkout(session, displayerID);
        }
    }

    private void sendNoActiveWorkout(Session session, Long displayerID) {
        String status = String.format(
                "{\"displayerID\":%d,\"active\":false}",
                displayerID
        );

        try {
            session.getBasicRemote().sendText(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastWorkoutStatus(Long userID) {
        Session session = userSessions.get(userID);
        if (session != null && session.isOpen()) {
            sendWorkoutStatus(session, userID);
        }
    }



}
