package CyTrack.GroupChatPackage;

import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import CyTrack.Responses.ChatMessageResponse;
import CyTrack.Services.UserService;
import CyTrack.Sockets.NotificationsWebSocket;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import CyTrack.Services.NotificationService;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@ServerEndpoint("/groupchat/{groupID}/{userID}")
public class GroupWebSocket {

    private static GroupChatService groupChatService;
    private static UserService userService;
    private static UserRepository userRepository;
    private static NotificationService notificationService;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    @Autowired
    public GroupWebSocket(GroupChatService groupChatService, UserService userService, UserRepository userRepository, NotificationService notificationService) {
        GroupWebSocket.groupChatService = groupChatService;
        GroupWebSocket.userService = userService;
        GroupWebSocket.userRepository = userRepository;
        GroupWebSocket.notificationService = notificationService;
    }

    public GroupWebSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("groupID") Long groupID, @PathParam("userID") Long userID) throws IOException {
        Optional<GroupChat> groupChat = groupChatService.getGroupChatWithMessages(groupID);
        if (groupChat.isPresent() && groupChat.get().getMembers().contains(userRepository.findById(userID).orElse(null))) {
            sessionUserMap.put(session, userID);
            userSessionMap.computeIfAbsent(userID, k -> new ArrayList<>()).add(session);

            // Load chat history from the database
            List<GroupMessage> messageHistory = groupChatService.getChatHistory(groupID);

            for (GroupMessage message : messageHistory) {
                ChatMessageResponse response = new ChatMessageResponse(
                        "success",
                        new ChatMessageResponse.Data(message.getUser().getUserID(), message.getMessage()),
                        "Chat history loaded"
                );
                String jsonResponse = objectMapper.writeValueAsString(response);
                session.getBasicRemote().sendText(jsonResponse);
            }
        } else {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "You are not a member of this group"));
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("groupID") Long groupID, String messageText) throws IOException {
        Long senderID = sessionUserMap.get(session);
        if (senderID == null) return;

        Optional<GroupChat> groupChat = groupChatService.getGroupChat(groupID);
        if (groupChat.isPresent() && groupChat.get().getMembers().contains(userRepository.findById(senderID).orElse(null))) {
            User sender = userRepository.findById(senderID).get();
            GroupMessage groupMessage = new GroupMessage();
            groupMessage.setUser(sender);
            groupMessage.setGroupChat(groupChat.get());
            groupMessage.setMessage(messageText);
            groupChatService.saveMessage(groupMessage);

            CustomObject customMessage = new CustomObject(sender.getUsername(), messageText, groupMessage.getDate());
            String jsonMessage = objectMapper.writeValueAsString(customMessage);

            for (User member : groupChat.get().getMembers()) {
                List<Session> memberSessions = userSessionMap.get(member.getUserID());
                if (memberSessions != null) {
                    for (Session memberSession : memberSessions) {
                        if (memberSession.isOpen()) {
                            if (member.getUserID().equals(senderID)) {
                                memberSession.getBasicRemote().sendText("You: " + messageText);
                            } else {
                                memberSession.getBasicRemote().sendText(sender.getUsername() + ": " + messageText);
                            }
                        }
                    }
                } else {
                    // Notify offline users
                    notificationService.notifyUser(member.getUserID(), jsonMessage);
                }

                // Broadcast update to central WebSocket
                NotificationsWebSocket.broadcastUpdate(member.getUserID(), jsonMessage);
            }
        } else {
            session.getBasicRemote().sendText("Error: You are not a member of this group");
        }
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            List<Session> sessions = userSessionMap.get(userID);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty()) {
                    userSessionMap.remove(userID);
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }
}