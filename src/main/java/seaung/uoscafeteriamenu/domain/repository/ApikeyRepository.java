package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.Apikey;

import java.util.Optional;

public interface ApikeyRepository extends JpaRepository<Apikey, Long> {
    Optional<Apikey> findByBotApikey(String key);
}
