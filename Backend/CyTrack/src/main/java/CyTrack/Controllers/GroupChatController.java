package CyTrack.Controllers;

import CyTrack.DTO.CreateGroupChat;
import CyTrack.Entities.GroupChatMembers;
import CyTrack.Entities.GroupMessage;
import CyTrack.Entities.User;
import CyTrack.Repositories.GroupChatMembersRepository;
import CyTrack.Repositories.GroupMessageRepository;
import CyTrack.Repositories.UserRepository;
import CyTrack.Responses.CreateGroupChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupchat")
public class GroupChatController {
    @Autowired
    private GroupMessageRepository groupMessageRepository;

    @Autowired
    private GroupChatMembersRepository groupChatMembersRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createGroupChat(@RequestBody CreateGroupChat request){
        User admin = userRepository.findByUserID(request.getAdminID()).orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        GroupMessage groupMessage = new GroupMessage(admin, request.getGroupName());
        groupMessageRepository.save(groupMessage);

        for (Long userID : request.getMembers()){
            User user = userRepository.findByUserID(userID).orElseThrow(() -> new IllegalArgumentException("User not found"));
            groupChatMembersRepository.save(new GroupChatMembers(groupMessage, user));
        }
        CreateGroupChatResponse response = new CreateGroupChatResponse("Success", groupMessage.get().getGroupID(), "Group chat created successfully");
        return ResponseEntity.ok(response);
    }

}
