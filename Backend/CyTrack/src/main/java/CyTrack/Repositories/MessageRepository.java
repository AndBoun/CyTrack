package CyTrack.Repositories;

import CyTrack.Entities.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderID AND m.receiver.id = :receiverID) OR (m.sender.id = :receiverID AND m.receiver.id = :senderID) ORDER BY m.date DESC")
    List<Message> findLatestMessage(@Param("senderID") Long paramLong1, @Param("receiverID") Long paramLong2, Pageable paramPageable);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderID AND m.receiver.id = :receiverID) OR (m.sender.id = :receiverID AND m.receiver.id = :senderID) ORDER BY m.date ASC")
    List<Message> findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(@Param("senderID") Long paramLong1, @Param("receiverID") Long paramLong2);

    @Query("SELECT m FROM Message m WHERE (m.sender.userID = :userID OR m.receiver.userID = :userID) ORDER BY m.date DESC")
    List<Message> findDirectMessages(@Param("userID") Long paramLong);
}
