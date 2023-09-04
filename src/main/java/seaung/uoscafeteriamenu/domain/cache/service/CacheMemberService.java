package seaung.uoscafeteriamenu.domain.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheMemberService {

    private final MemberRepository memberRepository;
    private final CacheMemberRepository cacheMemberRepository;

    // 캐시에 데이터가 있으면 아래 로직을 수행하지 않음
    @Cacheable(value = "cacheMember", key = "#botUserId")
    public CacheMember findMemberOrCreateMemberByBotUserId(String botUserId) {

        Optional<Member> optionalMember = memberRepository.findByBotUserId(botUserId);

        // Database에 없으면 회원을 생성
        if(optionalMember.isEmpty()) {
            Member member = Member.create(botUserId, 0L);
            return CacheMember.of(memberRepository.save(member));
        }

        return CacheMember.of(optionalMember.get());
    }

    @CachePut(value = "cacheMember", key = "#cacheMember.botUserId")
    public CacheMember increaseCacheMemberVisitCount(CacheMember cacheMember) {
        cacheMember.increaseVisitCount();
        return cacheMemberRepository.save(cacheMember);
    }

    @Cacheable(value = "cacheMember")
    public List<CacheMember> findAllCacheMembers() {
        List<Member> members = memberRepository.findAll();
        List<CacheMember> cacheMembers = new ArrayList<>();
        for(Member m : members) {
            cacheMembers.add(CacheMember.of(m));
        }

        return cacheMembers;
    }
}
