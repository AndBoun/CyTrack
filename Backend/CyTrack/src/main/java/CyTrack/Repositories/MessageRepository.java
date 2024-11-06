package CyTrack.Repositories;

import CyTrack.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(Long senderID, Long receiverID, Long receiverIDAlt, Long senderIDAlt);
}
