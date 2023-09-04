package seaung.uoscafeteriamenu.domain.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import seaung.uoscafeteriamenu.domain.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByBotUserId(String botUserId);
    List<Member> findByBotUserIdIn(List<String> botUserIds);
}
