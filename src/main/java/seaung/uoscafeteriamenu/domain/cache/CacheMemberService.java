package seaung.uoscafeteriamenu.domain.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;

@Service
@RequiredArgsConstructor
public class CacheMemberService {

    private final CacheMemberRepository cacheMemberRepository;
}
