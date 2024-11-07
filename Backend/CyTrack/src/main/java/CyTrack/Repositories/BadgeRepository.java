package CyTrack.Repositories;

import CyTrack.Entities.Badge;
import CyTrack.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByBadgeID(Long badgeID);

    Optional<Badge> findByBadgeName(String badgeName);

    Optional<Badge> findByUserAndBadgeName(User user, String badgeName);

}
