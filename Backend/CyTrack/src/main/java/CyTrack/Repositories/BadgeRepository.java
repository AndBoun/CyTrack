package CyTrack.Repositories;

import CyTrack.Entities.Badges.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {


}
