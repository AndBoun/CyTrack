package CyTrack.Repositories;

import CyTrack.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.senderID = :userID AND m.receiverID = :friendUserID) OR (m.senderID = :friendUserID AND m.receiverID = :userID) ORDER BY m.date DESC")
    Optional<Message> findLatestMessage(@Param("userID") Long userID, @Param("friendUserID") Long friendUserID);

    List<Message> findBySenderIDAndReceiverIDOrReceiverIDAndSenderIDOrderByDateAsc(Long user1ID, Long user2ID, Long user2ID1, Long user1ID1);
}