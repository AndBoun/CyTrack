package CyTrack.Sockets;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class FriendsNotificationSocket {

    @MessageMapping("/sendFriendRequest")
    @SendToUser("/queue/friendRequest")
    public String sendNotification(String notification) {
        return notification;
    }
}