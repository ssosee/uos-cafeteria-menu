package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
