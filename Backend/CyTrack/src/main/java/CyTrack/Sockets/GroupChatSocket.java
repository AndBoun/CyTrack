package CyTrack.Sockets;

import CyTrack.Entities.GroupMessage;
import CyTrack.Repositories.GroupMessageRepository;
import CyTrack.Responses.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import CyTrack.Services.UserService;

import java.io.IOException;
import java.util.*;

@Controller
@ServerEndpoint("/groupchat/{groupID}/{userID}")
public class GroupChatSocket {

    private static GroupMessageRepository groupMsgRepo;
    private static UserService userService;

    private static final Map<Long, Set<Long>> groupMembers = new Hashtable<>();
    private static final Map<Long, Long> groupAdmins = new Hashtable<>();
    private static final Map<Long, Session> userSessionMap = new Hashtable<>();
    private static final Map<Session, Long> sessionUserMap = new Hashtable<>();

    @Autowired
    public GroupChatSocket(GroupMessageRepository groupMessageRepository, UserService userService) {
        GroupChatSocket.groupMsgRepo = groupMessageRepository;
        GroupChatSocket.userService = userService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("groupID") Long groupID, @PathParam("userID") Long userID) throws IOException {
        if (groupMembers.containsKey(groupID) && groupMembers.get(groupID).contains(userID)) {
            sessionUserMap.put(session, userID);
            userSessionMap.put(userID, session);
            sendChatHistory(session, groupID);
        } else {
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "You are not a member of this group"));
        }
    }

    @OnMessage
    public void onMessage(Session session, @PathParam("groupID") Long groupID, String messageText) throws IOException {
        Long userID = sessionUserMap.get(session);
        if (userID == null) return;

        if (groupMembers.containsKey(groupID) && groupMembers.get(groupID).contains(userID)) {
            String username = userService.findByUserID(userID).orElseThrow(() -> new IllegalArgumentException("User not found")).getUsername();
            GroupMessage message = new GroupMessage(userID, groupID, username, messageText);
            groupMsgRepo.save(message);

            broadcastMessage(groupID, username, messageText);
        } else {
            session.getBasicRemote().sendText("Error: You are not a member of this group");
        }
    }

    @OnClose
    public void onClose(Session session) {
        Long userID = sessionUserMap.remove(session);
        if (userID != null) {
            userSessionMap.remove(userID);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    public void createGroup(Long groupID, Long adminID) {
        groupMembers.putIfAbsent(groupID, new HashSet<>());
        groupAdmins.put(groupID, adminID);
        addUserToGroup(groupID, adminID);
    }

    public void addUserToGroup(Long groupID, Long userID) {
        groupMembers.computeIfAbsent(groupID, k -> new HashSet<>()).add(userID);
    }

    public void removeUserFromGroup(Long groupID, Long adminID, Long userID) throws IOException {
        if (!groupAdmins.getOrDefault(groupID, -1L).equals(adminID)) {
            throw new SecurityException("Only admins can remove users from the group");
        }
        if (groupMembers.containsKey(groupID)) {
            groupMembers.get(groupID).remove(userID);
            closeUserSession(userID, "Removed by admin");
        }
    }

    private void sendChatHistory(Session session, Long groupID) throws IOException {
        var chatHistory = groupMsgRepo.findByGroupIDOrderByDateAsc(groupID);
        for (GroupMessage msg : chatHistory) {
            ChatMessageResponse chatMessage = new ChatMessageResponse("success", new ChatMessageResponse.Data(msg.getSenderID(), msg.getContent()), "Chat history loaded");
            String jsonMessage = new ObjectMapper().writeValueAsString(chatMessage);
            session.getBasicRemote().sendText(jsonMessage);
        }
    }

    private void broadcastMessage(Long groupID, String username, String messageText) throws IOException {
        for (Long memberID : groupMembers.getOrDefault(groupID, Collections.emptySet())) {
            Session memberSession = userSessionMap.get(memberID);
            if (memberSession != null && memberSession.isOpen()) {
                memberSession.getBasicRemote().sendText(username + ": " + messageText);
            }
        }
    }

    private void closeUserSession(Long userID, String reason) throws IOException {
        Session session = userSessionMap.get(userID);
        if (session != null && session.isOpen()) {
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, reason));
        }
    }
}
