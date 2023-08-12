package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.Utterance;

public interface UtteranceRepository extends JpaRepository<Utterance, Long> {
}
