package CyTrack.Repositories;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.Friends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<Friends, Long> {


    boolean existsByUser1_UserIDAndUser2_UserID(Long friendID, Long userID);
}