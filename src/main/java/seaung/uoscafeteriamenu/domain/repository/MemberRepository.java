package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
