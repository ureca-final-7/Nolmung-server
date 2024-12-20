package ureca.nolmung.persistence.badge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ureca.nolmung.jpa.badge.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByUserId(Long userId);
    boolean existsByBadgeCodeIdAndUserId(Long badgeCodeId, Long userId);
}
