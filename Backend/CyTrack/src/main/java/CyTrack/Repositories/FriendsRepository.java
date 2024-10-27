package CyTrack.Repositories;
import CyTrack.Entities.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, Long> {
    Friends findByFriendID(Long friendID);

    void deleteByFriendID(Long friendID);
}
