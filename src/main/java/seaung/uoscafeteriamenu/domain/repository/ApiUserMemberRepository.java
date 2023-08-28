package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.ApiUseMember;

public interface ApiUserMemberRepository extends JpaRepository<ApiUseMember, Long> {
}
