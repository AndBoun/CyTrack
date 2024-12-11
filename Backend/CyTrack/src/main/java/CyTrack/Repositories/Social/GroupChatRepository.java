package CyTrack.Repositories.Social;

import CyTrack.Entities.GroupChat;
import CyTrack.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    List<GroupChat> findByMembers(User user);

    List<GroupChat> findByMembersContaining(User user);
}
