package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.util.Optional;

public interface CrawlingTargetRepository extends JpaRepository<CrawlingTarget, Long> {
    Optional<CrawlingTarget> findByRestaurantsName(UosRestaurantName restaurantsName);
}
