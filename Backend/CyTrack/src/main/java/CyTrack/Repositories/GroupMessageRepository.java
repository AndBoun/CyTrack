package CyTrack.Repositories;

import CyTrack.Entities.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {
    List<GroupMessage> findByGroupIDOrderByDateAsc(Long groupID);
}