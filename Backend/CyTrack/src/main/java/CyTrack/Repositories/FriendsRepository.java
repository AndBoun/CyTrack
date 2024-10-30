package CyTrack.Repositories;

import CyTrack.Entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendsRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderUserID(Long userID);
    boolean existsByReceiverUserID(Long userID);
}