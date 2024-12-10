package CyTrack.GroupChatPackage;
import CyTrack.Entities.User;
import CyTrack.Repositories.UserRepository;
import CyTrack.Services.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import CyTrack.GroupChatPackage.GroupChatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class GroupChatService {

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendsService friendsService;

    public GroupChat createGroupChat(User admin, String groupName) {
        GroupChat groupChat = new GroupChat();
        groupChat.setAdmin(admin);
        groupChat.setGroupName(groupName);
        groupChat.addMember(admin);
        return groupChatRepository.save(groupChat);
    }

    public GroupChat addMember(Long groupChatID, Long userID, Long requesterID) {
        Optional<GroupChat> groupChatOpt = groupChatRepository.findById(groupChatID);
        Optional<User> userOpt = userRepository.findById(userID);
        Optional<User> requesterOpt = userRepository.findById(requesterID);

        if (groupChatOpt.isPresent() && userOpt.isPresent() && requesterOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            User user = userOpt.get();
            User requester = requesterOpt.get();
            if (friendsService.checkIfFriends(requester.getUserID(), user.getUserID())) {
                if (groupChat.getMembers().contains(user)) {
                    return null; // User is already a member
                }
                groupChat.addMember(user);
                return groupChatRepository.save(groupChat);
            }
        }
        return null;
    }


    public GroupChat removeMember(Long groupChatID, Long userID) {
        Optional<GroupChat> groupChatOpt = groupChatRepository.findById(groupChatID);
        Optional<User> userOpt = userRepository.findById(userID);

        if (groupChatOpt.isPresent() && userOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            User user = userOpt.get();
            groupChat.removeMember(user);
            return groupChatRepository.save(groupChat);
        }
        return null;
    }

    public Optional<GroupChat> getGroupChat(Long groupChatID) {
        Optional<GroupChat> groupChatOpt = groupChatRepository.findById(groupChatID);
        if (groupChatOpt.isPresent()) {
            return groupChatOpt;
        }
        return Optional.empty();
    }

    public GroupChat updateGroupName(Long groupChatID, String groupName) {
        Optional<GroupChat> groupChatOpt = groupChatRepository.findById(groupChatID);
        if (groupChatOpt.isPresent()) {
            GroupChat groupChat = groupChatOpt.get();
            groupChat.setGroupName(groupName);
            return groupChatRepository.save(groupChat);
        }
        return null;
    }

    public List<GroupChat> getGroupChats(Long userID) {
        Optional<User> userOpt = userRepository.findById(userID);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return groupChatRepository.findByMembers(user);
        }
        return new ArrayList<>();
    }
}
