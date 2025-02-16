package CyTrack.Sockets;

import CyTrack.Entities.Message;
import CyTrack.Entities.User;
import CyTrack.Entities.GroupChat;
import CyTrack.Entities.GroupMessage;
import CyTrack.Repositories.MessageRepository;
import CyTrack.Repositories.Social.GroupChatRepository;
import CyTrack.Responses.Social.MessageResponse;
import CyTrack.Services.Social.FriendsService;
import CyTrack.Services.NotificationService;
import CyTrack.Services.UserService;
import CyTrack.Services.Social.GroupChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@ServerEndpoint("/chat/{chatType}/{chatID}/{userID}")
public class ChatWebSocket {

    private static MessageRepository msgRepo;
    private static GroupChatRepository groupChatRepo;
    private static GroupChatService groupChatService;
    private static FriendsService friendsService;
    private static UserService userService;
    private static NotificationService notificationService;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private static Map<Session, Long> sessionUserMap = new ConcurrentHashMap<>();
    private static Map<Long, List<Session>> userSessionMap = new ConcurrentHashMap<>();
    private static Map<Long, String> userActiveChatType = new ConcurrentHashMap<>();
    private static Map<Long, Long> userActiveChatID = new ConcurrentHashMap<>();

    @Autowired
    public ChatWebSocket(MessageRepository msgRepo, GroupChatRepository groupChatRepo, GroupChatService groupChatService, FriendsService friendsService, UserService userService, NotificationService notificationService) {
        ChatWebSocket.msgRepo = msgRepo;
        ChatWebSocket.groupChatRepo = groupChatRepo;
        ChatWebSocket.groupChatService = groupChatService;
        ChatWebSocket.friendsService = friendsService;
        ChatWebSocket.userService = userService;
        ChatWebSocket.notificationService = notificationService;
    }

    public ChatWebSocket() {}

    @OnOpen
    public void onOpen(Session session, @PathParam("chatType") String chatType, @PathParam("chatID") Long chatID, @PathParam("userID") Long userID) throws IOException {
        userActiveChatType.put(userID, chatType);
        userActiveChatID.put(userID, chatID);
        if ("group".equals(chatType)) {
            Optional<GroupChat> groupChat = groupChatRepo.findById(chatID);
            if (groupChat.isPresent() && groupChat.get().getMembers().contains(userService.findByUserID(userID).orElse(null))) {
                sessionUserMap.put(session, userID);
                userSessionMap.computeIfAbsent(userID, k -> new ArrayList<>()).add(session);

                List<GroupMessage> messageHistory = groupChatService.getChatHistory(chatID);
                String groupName = groupChat.get().getGroupName();
                for (GroupMessage message : messageHistory) {
                    String time = new SimpleDateFormat("HH:mm:ss").format(message.getDate());
                    LocalDateTime timestamp = message.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    MessageResponse response = new MessageResponse(
                            "success",
                            new MessageResponse.Data("group", message.getUser().getUsername(), null, groupName, message.getMessage(), time, chatID, message.getUser().getUserID()),
                            "Chat history loaded"
                    );
                      String jsonResponse = objectMapper.writeValueAsString(response);
                    session.getBasicRemote().sendText(jsonResponse);
                }
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "You are not a member of this group"));
            }
        } else if ("direct".equals(chatType)) {
            Long receiverID = chatID;
            if (friendsService.checkIfFriends(userID, receiverID)) {
                sessionUserMap.put(session, userID);
                userSessionMap.computeIfAbsent(userID, k -> new ArrayList<>()).add(session);

                List<Message> messageHistory = msgRepo.findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(userID, receiverID);
                for (Message message : messageHistory) {
                    String time = new SimpleDateFormat("HH:mm:ss").format(message.getDate());
                    MessageResponse response = new MessageResponse(
                            "success",
                            new MessageResponse.Data("direct", message.getSender().getUsername(), message.getReceiver().getUsername(), null, message.getContent(), time, chatID, message.getSender().getUserID()),
                            "Chat history loaded"
                    );
                    String jsonResponse = objectMapper.writeValueAsString(response);
                    session.getBasicRemote().sendText(jsonResponse);
                }
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "You are not friends with this user"));
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("chatType") String chatType, @PathParam("chatID") Long chatID, String messageText) throws IOException {
        Long senderID = sessionUserMap.get(session);
        if (senderID == null) return;

        if ("group".equals(chatType)) {
            handleGroupMessage(session, chatID, messageText, senderID);
        } else if ("direct".equals(chatType)) {
            handleDirectMessage(session, chatID, messageText, senderID);
        }
    }

    private void handleGroupMessage(Session session, Long chatID, String messageText, Long senderID) {
        CompletableFuture.runAsync(() -> {
            try {
                Optional<GroupChat> groupChat = groupChatRepo.findById(chatID);
                if (groupChat.isPresent() && groupChat.get().getMembers().contains(userService.findByUserID(senderID).orElse(null))) {
                    User sender = userService.findByUserID(senderID).get();
                    GroupMessage groupMessage = new GroupMessage();
                    groupMessage.setUser(sender);
                    groupMessage.setGroupChat(groupChat.get());
                    groupMessage.setMessage(messageText);
                    groupChatService.saveMessage(groupMessage);

                    String senderUsername = sender.getUsername();
                    String groupName = groupChat.get().getGroupName();
                    String time = new SimpleDateFormat("HH:mm:ss").format(groupMessage.getDate());
                    MessageResponse messageResponse = new MessageResponse(
                            "success",
                            new MessageResponse.Data("group", senderUsername, null, groupName, messageText, time, chatID, senderID),
                            "New message received"
                    );
                    String jsonMessage = objectMapper.writeValueAsString(messageResponse);

                    for (User member : groupChat.get().getMembers()) {
                        List<Session> memberSessions = userSessionMap.get(member.getUserID());
                        if (memberSessions != null) {
                            for (Session memberSession : memberSessions) {
                                Long memberID = sessionUserMap.get(memberSession);
                                if (memberID != null
                                        && chatID.equals(userActiveChatID.get(memberID))
                                        && "group".equals(userActiveChatType.get(memberID))) {
                                    if (memberSession.isOpen()) {
                                        if (member.getUserID().equals(senderID)) {
                                            memberSession.getBasicRemote().sendText("You: " + messageText);
                                        } else {
                                            memberSession.getBasicRemote().sendText(senderUsername + ": " + messageText);
                                        }
                                    }
                                } else {
                                    notificationService.notifyUser(member.getUserID(), jsonMessage);
                                }
                            }
                        } else {
                            notificationService.notifyUser(member.getUserID(), jsonMessage);
                        }

                        NotificationsWebSocket.broadcastUpdate(member.getUserID(), jsonMessage);
                    }
                } else {
                    session.getBasicRemote().sendText("Error: You are not a member of this group");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executorService);
    }


    private void handleDirectMessage(Session session, Long chatID, String messageText, Long senderID) {
        CompletableFuture.runAsync(() -> {
            try {
                Long receiverID = chatID;
                if (friendsService.checkIfFriends(senderID, receiverID)) {
                    String senderUsername = userService.findByUserID(senderID).get().getUsername();
                    String receiverUsername = userService.findByUserID(receiverID).get().getUsername();
                    Message message = new Message(senderID, receiverID, senderUsername, receiverUsername, messageText);
                    msgRepo.save(message);

                    String time = new SimpleDateFormat("HH:mm:ss").format(message.getDate());
                    MessageResponse messageResponse = new MessageResponse(
                            "success",
                            new MessageResponse.Data("direct", senderUsername, receiverUsername, null, messageText, time, chatID, senderID),
                            "New message received"
                    );
                    String jsonMessage = objectMapper.writeValueAsString(messageResponse);

                    List<Session> receiverSessions = userSessionMap.get(receiverID);
                    if (receiverSessions != null) {
                        for (Session receiverSession : receiverSessions) {
                            if (receiverSession.isOpen()) {
                                receiverSession.getBasicRemote().sendText(senderUsername + ": " + messageText);
                            }
                        }
                    } else {
                        notificationService.notifyUser(receiverID, jsonMessage);
                    }

                    session.getBasicRemote().sendText("You: " + messageText);
                    NotificationsWebSocket.broadcastUpdate(receiverID, jsonMessage);
                } else {
                    session.getBasicRemote().sendText("Error: You are not friends with this user");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, executorService);
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            userActiveChatType.remove(userID);
            userActiveChatID.remove(userID);

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