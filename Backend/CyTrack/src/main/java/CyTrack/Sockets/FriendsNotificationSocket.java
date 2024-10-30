package CyTrack.Sockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FriendsNotificationSocket {

    @MessageMapping("/sendFriendRequest")
    @SendTo("/topic/notifications")
    public String sendNotification(String notification) {
        return notification;
    }
}