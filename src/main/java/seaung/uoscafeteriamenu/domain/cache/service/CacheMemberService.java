package seaung.uoscafeteriamenu.domain.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheMemberService {

    private final MemberRepository memberRepository;
    private final CacheMemberRepository cacheMemberRepository;

    @Transactional
    @Cacheable(value = "cacheMember", key = "#botUserId")
    public CacheMember findMemberOrCreateMemberByBotUserId(String botUserId) {

        Optional<Member> optionalMember = memberRepository.findByBotUserId(botUserId);

        // Database에 없으면 회원을 생성
        if(optionalMember.isEmpty()) {
            Member member = Member.create(botUserId, 1L);
            return CacheMember.of(memberRepository.save(member));
        }

        // Database에 있으면 cache의 visitCount 1증가
        CacheMember cacheMember = CacheMember.of(optionalMember.get());
        cacheMember.increaseVisitCount();
        System.out.println("cacheMember.getVisitCount()="+cacheMember.getVisitCount());

        return cacheMember;
    }

    @Cacheable(value = "cacheMember", key = "#botUserId")
    public CacheMember findCacheMember(String botUserId) {
        Member member = memberRepository.findByBotUserId(botUserId).orElseThrow();
        return CacheMember.of(member);
    }

    @Transactional
    @CachePut(value = "cacheMember", key = "#botUserId")
    public CacheMember increaseCacheMemberVisitCount(String botUserId) {
        Member member = memberRepository.findByBotUserId(botUserId).orElseThrow();
        member.increaseVisitCount();

        return CacheMember.of(member);
    }
}
