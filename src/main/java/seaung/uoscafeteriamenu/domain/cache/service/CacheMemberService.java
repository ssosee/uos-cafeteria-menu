package seaung.uoscafeteriamenu.domain.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheMemberService {

    private final MemberRepository memberRepository;

    // 캐시에 데이터가 있으면 아래 로직을 수행하지 않음
    @Cacheable(value = "cacheMember", key = "#botUserId")
    public CacheMember findMemberOrCreateMemberByBotUserId(String botUserId) {
        // 회원 조회
        Optional<Member> optionalMember = memberRepository.findByBotUserId(botUserId);

        // Database에 없으면 회원을 생성
        if(optionalMember.isEmpty()) {
            Member member = Member.create(botUserId, 0L);
            return CacheMember.of(memberRepository.save(member));
        }

        return CacheMember.of(optionalMember.get());
    }

    // 캐시에 저장(로직을 항상 수행함)
    @CachePut(value = "cacheMember", key = "#cacheMember.botUserId")
    public CacheMember increaseCacheMemberVisitCount(CacheMember cacheMember) {
        cacheMember.increaseVisitCount();
        return cacheMember;
    }
}
