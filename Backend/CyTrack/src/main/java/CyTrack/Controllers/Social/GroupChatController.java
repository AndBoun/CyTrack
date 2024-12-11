package CyTrack.Controllers.Social;

import CyTrack.Entities.GroupChat;
import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import CyTrack.Responses.Social.GroupChatListResponse;
import CyTrack.Responses.Social.GroupChatMemberResponse;
import CyTrack.Responses.Social.GroupChatResponse;
import CyTrack.Services.Social.FriendsService;
import CyTrack.Services.Social.GroupChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{userID}/groupchat")
public class GroupChatController {

    @Autowired
    private GroupChatService groupChatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsService friendsService;


    @PostMapping("/create")
    public ResponseEntity<?> createGroupChat(@PathVariable Long userID, @RequestBody Map<String, String> requestBody) {
        String groupName = requestBody.get("groupName");
        Optional<User> adminOpt = userRepository.findById(userID);
        if (adminOpt.isPresent()) {
            User admin = adminOpt.get();
            GroupChat groupChat = groupChatService.createGroupChat(admin, groupName);
            GroupChatResponse.Data data = new GroupChatResponse.Data(groupChat.getGroupChatID(), groupChat.getGroupName(), admin.getUserID());
            GroupChatResponse response = new GroupChatResponse("success", data, "Group chat created successfully");
            return ResponseEntity.status(201).body(response);
        }
        GroupChatResponse response = new GroupChatResponse("error", null, "Admin not found");
        return ResponseEntity.status(404).body(response);
    }

    @PostMapping("/addMember")
    public ResponseEntity<?> addMember(@PathVariable Long userID, @RequestBody Map<String, Long> requestBody) {
        Long groupChatID = requestBody.get("groupChatID");
        Long memberID = requestBody.get("userID");
        GroupChat groupChat = groupChatService.addMember(groupChatID, memberID, userID);
        if (groupChat != null) {
            GroupChatMemberResponse.Data data = new GroupChatMemberResponse.Data(
                    groupChat.getGroupChatID(),
                    groupChat.getGroupName(),
                    groupChat.getAdmin().getUserID(),
                    groupChat.getMembers().stream()
                            .map(member -> new GroupChatMemberResponse.Data.Member(member.getUserID(), member.getUsername()))
                            .collect(Collectors.toList())
            );
            GroupChatMemberResponse response = new GroupChatMemberResponse("success", data, "Member added successfully");
            return ResponseEntity.status(200).body(response);
        }
        GroupChatResponse response = new GroupChatResponse("error", null, "Group chat or user not found or not friends or already a member");
        return ResponseEntity.status(404).body(response);
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<?> removeMember(@PathVariable Long userID, @RequestBody Map<String, Long> requestBody) {
        Long groupChatID = requestBody.get("groupChatID");
        Long memberID = requestBody.get("userID");

        Optional<GroupChat> groupChatOpt = groupChatService.getGroupChat(groupChatID);
        if (groupChatOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            if (!groupChat.getAdmin().getUserID().equals(userID)) {
                GroupChatResponse response = new GroupChatResponse("error", null, "Only the admin can remove members");
                return ResponseEntity.status(403).body(response);
            }
            GroupChat updatedGroupChat = groupChatService.removeMember(groupChatID, memberID);
            if (updatedGroupChat != null) {
                GroupChatMemberResponse.Data data = new GroupChatMemberResponse.Data(
                        updatedGroupChat.getGroupChatID(),
                        updatedGroupChat.getGroupName(),
                        updatedGroupChat.getAdmin().getUserID(),
                        updatedGroupChat.getMembers().stream()
                                .map(member -> new GroupChatMemberResponse.Data.Member(member.getUserID(), member.getUsername()))
                                .collect(Collectors.toList())
                );
                GroupChatMemberResponse response = new GroupChatMemberResponse("success", data, "Member removed successfully");
                return ResponseEntity.status(200).body(response);
            }
        }
        GroupChatResponse response = new GroupChatResponse("error", null, "Group chat or user not found");
        return ResponseEntity.status(404).body(response);
    }

    @GetMapping("/getMembers")
    public ResponseEntity<?> getMembers(@RequestBody Map<String, Long> requestBody) {
        Long groupChatID = requestBody.get("groupChatID");
        Optional<GroupChat> groupChatOpt = groupChatService.getGroupChat(groupChatID);
        if (groupChatOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            GroupChatMemberResponse.Data data = new GroupChatMemberResponse.Data(
                    groupChat.getGroupChatID(),
                    groupChat.getGroupName(),
                    groupChat.getAdmin().getUserID(),
                    groupChat.getMembers().stream()
                            .map(member -> new GroupChatMemberResponse.Data.Member(member.getUserID(), member.getUsername()))
                            .collect(Collectors.toList())
            );
            GroupChatMemberResponse response = new GroupChatMemberResponse("success", data, "Members retrieved successfully");
            return ResponseEntity.status(200).body(response);
        }
        GroupChatResponse response = new GroupChatResponse("error", null, "Group chat not found");
        return ResponseEntity.status(404).body(response);
    }

    @PutMapping("/updateGroupName")
    public ResponseEntity<?> updateGroupName(@PathVariable Long userID, @RequestBody Map<String, String> requestBody) {
        Long groupChatID = Long.parseLong(requestBody.get("groupChatID"));
        String groupName = requestBody.get("groupName");
        Optional<GroupChat> groupChatOpt = groupChatService.getGroupChat(groupChatID);
        if (groupChatOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            if (!groupChat.getAdmin().getUserID().equals(userID)) {
                GroupChatResponse response = new GroupChatResponse("error", null, "Only the admin can update the group name");
                return ResponseEntity.status(403).body(response);
            }
            groupChat.setGroupName(groupName);
            GroupChat updatedGroupChat = groupChatService.updateGroupName(groupChatID, groupName);
            GroupChatResponse.Data data = new GroupChatResponse.Data(updatedGroupChat.getGroupChatID(), updatedGroupChat.getGroupName(), updatedGroupChat.getAdmin().getUserID());
            GroupChatResponse response = new GroupChatResponse("success", data, "Group name updated successfully");
            return ResponseEntity.status(200).body(response);
        }
        GroupChatResponse response = new GroupChatResponse("error", null, "Group chat not found");
        return ResponseEntity.status(404).body(response);
    }

    //Get all group chats a user is in
    @GetMapping("")
    public ResponseEntity<?> getGroupChats(@PathVariable Long userID) {
        Optional<User> userOpt = userRepository.findById(userID);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<GroupChat> groupChats = groupChatService.getGroupChats(userID);
            List<GroupChatListResponse.Data> data = groupChats.stream()
                    .map(groupChat -> new GroupChatListResponse.Data(groupChat.getGroupChatID(), groupChat.getGroupName(), groupChat.getAdmin().getUserID()))
                    .collect(Collectors.toList());
            GroupChatListResponse response = new GroupChatListResponse("success", data, "Group chats retrieved successfully");
            return ResponseEntity.status(200).body(response);
        }
        GroupChatListResponse response = new GroupChatListResponse("error", null, "User not found");
        return ResponseEntity.status(404).body(response);
    }



}
