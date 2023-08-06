package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;

public interface UosRestaurantCrawlingTargetRepository extends JpaRepository<CrawlingTarget, Long> {

}
