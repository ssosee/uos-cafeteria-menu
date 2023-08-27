package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member registerMemberOrIncreaseMemberViewCount(String botUserId) {
        // 회원 조회
        Optional<Member> findMember = memberRepository.findByBotUserId(botUserId);

        // 회원이 없으면 회원을 생성
        if(findMember.isEmpty()) {
            Member member = Member.create(botUserId, 1L);
            return memberRepository.save(member);
        }
        // 회원이 있으면 방문횟수 증가
        else {
            Member member = findMember.get();
            member.increaseVisitCount();

            return member;
        }
    }
}
