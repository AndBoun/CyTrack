package CyTrack.Repositories;

import CyTrack.Entities.User;
import CyTrack.Entities.UserConversations;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserID(Long userID);

    // Custom query to fetch User with badges
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.badges WHERE u.userID = :userID")
    Optional<User> findByUserIDWithBadges(@Param("userID") Long userID);
}