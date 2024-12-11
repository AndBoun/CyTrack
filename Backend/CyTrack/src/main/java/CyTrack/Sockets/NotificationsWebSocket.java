package CyTrack.Sockets;

import CyTrack.Entities.GroupChat;
import CyTrack.Entities.GroupMessage;
import CyTrack.Entities.Message;
import CyTrack.Entities.User;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Repositories.Social.GroupChatRepository;
import CyTrack.Responses.Social.MessageResponse;
import CyTrack.Services.Social.GroupChatService;
import CyTrack.Services.UserService;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/notifications/{userID}")
public class NotificationsWebSocket {
    private static UserService userService;

    private static GroupChatService groupChatService;

    private static MessageRepository messageRepository;

    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();

    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();

    private static final ObjectMapper objectMapper = (new ObjectMapper()).registerModule((Module)new JavaTimeModule());

    private static GroupChatRepository groupChatRepo;

    @Autowired
    public NotificationsWebSocket(UserService userService, MessageRepository messageRepository, GroupChatRepository groupChatRepo, GroupChatService groupChatService) {
        NotificationsWebSocket.userService = userService;
        NotificationsWebSocket.messageRepository = messageRepository;
        NotificationsWebSocket.groupChatRepo = groupChatRepo;
        NotificationsWebSocket.groupChatService = groupChatService;
    }

    public NotificationsWebSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") Long userID) throws IOException {
        Optional<User> user = userService.findByUserID(userID);
        if (!user.isPresent()) {
            session.close(new CloseReason((CloseReason.CloseCode)CloseReason.CloseCodes.CANNOT_ACCEPT, "User not found"));
            return;
        }
        sessionUserMap.put(session, userID);
        ((List<Session>)userSessionMap.computeIfAbsent(userID, k -> new ArrayList())).add(session);
        List<MessageResponse> allMessages = new ArrayList<>();
        List<GroupChat> groupChats = groupChatRepo.findByMembersContaining(user.get());
        for (GroupChat groupChat : groupChats) {
            List<GroupMessage> groupMessages = groupChatService.getChatHistory(groupChat.getGroupChatID());
            for (GroupMessage groupMessage : groupMessages) {
                String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(groupMessage.getDate());
                MessageResponse response = new MessageResponse("success", new MessageResponse.Data("group", groupMessage.getUser().getUsername(), null, groupChat.getGroupName(), groupMessage.getMessage(), time, groupChat.getGroupChatID(), groupMessage.getUser().getUserID()), "Chat history loaded");
                allMessages.add(response);
            }
        }
        List<Message> directMessages = messageRepository.findDirectMessages(userID);
        for (Message message : directMessages) {
            String time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(message.getDate());
            MessageResponse response = new MessageResponse("success", new MessageResponse.Data("direct", message.getSender().getUsername(), message.getReceiver().getUsername(), null, message.getContent(), time, message.getReceiver().getUserID(), message.getSender().getUserID()), "Chat history loaded");
            allMessages.add(response);
        }
        allMessages.sort((m1, m2) -> {
            try {
                Date date1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(m1.getData().getTime());
                Date date2 = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(m2.getData().getTime());
                return date1.compareTo(date2);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing message timestamp", e);
            }
        });
        for (MessageResponse messageResponse : allMessages) {
            String jsonResponse = objectMapper.writeValueAsString(messageResponse);
            session.getBasicRemote().sendText(jsonResponse);
        }
        System.out.println("WebSocket opened for userID: " + userID);
    }

    @OnMessage
    public void onMessage(Session session, String messageJson) throws IOException {}

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            List<Session> sessions = userSessionMap.get(userID);
            if (sessions != null) {
                sessions.remove(session);
                if (sessions.isEmpty())
                    userSessionMap.remove(userID);
            }
        }
        System.out.println("WebSocket closed for userID: " + userID);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    public static void broadcastUpdate(Long userID, String updateJson) throws IOException {
        System.out.println("Broadcasting update to userID: " + userID);
        List<Session> sessions = userSessionMap.get(userID);
        if (sessions != null)
            for (Session userSession : sessions) {
                if (userSession.isOpen()) {
                    System.out.println("Sending update to session: " + userSession.getId());
                    userSession.getBasicRemote().sendText(updateJson);
                }
            }
    }
}
