package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.MenuLike;

import java.util.Optional;

public interface MenuLikeRepository extends JpaRepository<MenuLike, Long> {
    Optional<MenuLike> findByMemberIdAndUosRestaurantId(Long memberId, Long uosRestaurantId);
}
