package seaung.uoscafeteriamenu.domain.cache.repository;

import org.springframework.data.repository.CrudRepository;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;

import java.util.Optional;

public interface CacheMemberRepository extends CrudRepository<CacheMember, String> {
}
