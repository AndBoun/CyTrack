package CyTrack.Sockets;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@ServerEndpoint("/workout/{userID}")
public class WorkoutTrackingSocket {

    private static Map<Session, Long> sessionUserMap = new Hashtable<>();
    private static Map<Long, Session> userSessionMap = new Hashtable<>();
    private static Map<Long, TimerTaskData> activeTimers = new Hashtable<>();

    private class TimerTask implements Runnable {
        private final Session session;
        private int seconds = 0, minutes = 0, hours = 0;

        public TimerTask(Session session) {
            this.session = session;
        }

        @Override
        public void run() {
            try {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes == 60) {
                    minutes = 0;
                    hours++;
                }
                // Send timer update to user
                session.getBasicRemote().sendText(String.format("Timer: %02d:%02d:%02d", hours, minutes, seconds));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stop() {
            Long userID = sessionUserMap.get(session);
            TimerTaskData timerData = activeTimers.get(userID);
            if (timerData != null && !timerData.executor.isShutdown()) {
                timerData.executor.shutdown();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") Long userID) {
        sessionUserMap.put(session, userID);
        userSessionMap.put(userID, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        Long userID = sessionUserMap.get(session);

        if (message.equalsIgnoreCase("start")) {
            if (!activeTimers.containsKey(userID)) {
                TimerTask timerTask = new TimerTask(session);
                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(timerTask, 0, 1, TimeUnit.SECONDS);
                activeTimers.put(userID, new TimerTaskData(timerTask, executor));

                notifyWorkoutStarted(userID); // Send workout started notification
            } else {
                session.getBasicRemote().sendText("Workout is already in progress.");
            }
        } else if (message.equalsIgnoreCase("stop")) {
            TimerTaskData timerData = activeTimers.remove(userID);
            if (timerData != null) {
                timerData.timerTask.stop();
                notifyWorkoutEnded(userID); // Send workout complete notification
            } else {
                session.getBasicRemote().sendText("No workout in progress.");
            }
        } else {
            session.getBasicRemote().sendText("Invalid command. Use 'start' to begin and 'stop' to end.");
        }
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            userSessionMap.remove(userID);
            TimerTaskData timerData = activeTimers.remove(userID);
            if (timerData != null) {
                timerData.timerTask.stop();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    public void notifyWorkoutStarted(Long userID) throws IOException {
        Session session = userSessionMap.get(userID);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText("Workout started!");
        }
    }

    public void notifyWorkoutEnded(Long userID) throws IOException {
        Session session = userSessionMap.get(userID);
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText("Workout complete!");
        }
    }

    private static class TimerTaskData {
        public final TimerTask timerTask;
        public final ScheduledExecutorService executor;

        public TimerTaskData(TimerTask timerTask, ScheduledExecutorService executor) {
            this.timerTask = timerTask;
            this.executor = executor;
        }
    }
}
