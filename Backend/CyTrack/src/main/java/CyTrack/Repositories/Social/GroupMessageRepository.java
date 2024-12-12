package CyTrack.Repositories.Social;

import CyTrack.Entities.GroupChat;
import CyTrack.Entities.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {

    GroupMessage findTopByGroupChat_GroupChatIDOrderByDateDesc(Long groupChatID);


}
