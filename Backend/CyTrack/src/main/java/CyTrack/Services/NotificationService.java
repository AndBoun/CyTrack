package CyTrack.Services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyUser(Long userID, String message) {
        // Logic to notify the user (e.g., push notification, email, etc.)
        System.out.println("Notifying user " + userID + ": " + message);
    }


}