package CyTrack.Repositories;

import CyTrack.Entities.Badges.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByID(Long badgeID);

    Optional<Badge> findByBadgeName(String badgeName);

}
