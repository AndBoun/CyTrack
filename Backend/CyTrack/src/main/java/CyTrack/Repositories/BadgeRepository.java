package CyTrack.Repositories;

import CyTrack.Entities.Badges.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Badge findByBadgeName(String badgeName);

}
