package CyTrack.Repositories;

import CyTrack.Entities.Badges.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Optional<Badge> findByBadgeID(Long badgeID);

    //Optional<Badge> findByBadgeName(String badgeName);

    // Custom query for finding by badgeName in the concrete class
    //@Query("SELECT b FROM LifetimeTimeBadge b WHERE b.badgeName = :badgeName")
    //Optional<LifetimeTimeBadge> findByBadgeName(String badgeName);

    Optional<Badge> findByBadgeName(String badgeName);


}
