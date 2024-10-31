package CyTrack.Repositories;

import CyTrack.Entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderUserID(Long userID);
    boolean existsByReceiverUserID(Long userID);
    boolean existsBySenderUserIDAndReceiverUserIDAndStatus(Long senderID, Long receiverID, FriendRequest.RequestStatus status);

    List<FriendRequest> findByReceiver_UserIDAndStatus(Long userID, FriendRequest.RequestStatus requestStatus);
}
