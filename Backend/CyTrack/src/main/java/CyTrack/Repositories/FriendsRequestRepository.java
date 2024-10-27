package CyTrack.Repositories;
import CyTrack.Entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findByFriendRequestID(Long friendRequestID);

    void deleteByFriendRequestID(Long friendRequestID);
}
