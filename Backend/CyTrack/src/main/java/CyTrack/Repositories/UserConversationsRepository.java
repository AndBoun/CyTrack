package CyTrack.Repositories;

import CyTrack.Entities.UserConversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConversationsRepository extends JpaRepository<UserConversations, Long> {
    List<UserConversations> findByUser1_UserIDOrUser2_UserID(Long user1ID, Long user2ID);
}