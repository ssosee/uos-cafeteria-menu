package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.BotApikey;

import java.util.Optional;

public interface ApikeyRepository extends JpaRepository<BotApikey, Long> {
    Optional<BotApikey> findByBotApikey(String key);
}
