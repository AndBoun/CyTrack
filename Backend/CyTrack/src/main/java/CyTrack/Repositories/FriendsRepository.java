package CyTrack.Repositories;

import CyTrack.Entities.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

    List<Friends> findByUser1_UserIDOrUser2_UserID(Long userID1, Long userID2);
    boolean existsByUser1_UserIDAndUser2_UserID(Long user1ID, Long user2ID);
}