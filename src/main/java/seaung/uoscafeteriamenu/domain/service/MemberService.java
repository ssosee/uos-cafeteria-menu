package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CacheMemberRepository cacheMemberRepository;

    public void registerMemberOrIncreaseMemberViewCount(String botUserId) {
        // 캐시에 회원이 존재하면 방문횟수 증가
        if(isCacheMemberInCacheAndIncreaseVisitCount(botUserId)) return;
        // 회원을 생성하고 DB와 cache에 저장
        createMemberInDatabaseAndCache(botUserId);
    }

    private boolean isCacheMemberInCacheAndIncreaseVisitCount(String botUserId) {
        // 캐시에서 회원 조회
        Optional<CacheMember> findCacheMember = cacheMemberRepository.findById(botUserId);
        // 캐시에 회원이 존재하면 방문횟수 증가
        if(findCacheMember.isPresent()) {
            CacheMember cacheMember = findCacheMember.get();
            cacheMember.increaseVisitCount();
            cacheMemberRepository.save(cacheMember);
            return true;
        }
        return false;
    }

    @Transactional
    public void createMemberInDatabaseAndCache(String botUserId) {
        // DB에서 회원 조회
        Optional<Member> findMember = memberRepository.findByBotUserId(botUserId);

        // 회원이 없으면 회원을 생성하고 DB와 cache에 저장
        if(findMember.isEmpty()) {
            Member newMember = Member.create(botUserId, 1L);
            memberRepository.save(newMember);

            CacheMember cacheMember = CacheMember.of(newMember);
            cacheMemberRepository.save(cacheMember);
        }
    }
}
