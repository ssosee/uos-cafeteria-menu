package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원이 없으면 회원을 생성한다.")
    void registerMember() {
        // given
        String botUserId = "1";

        // when
        memberService.registerMemberOrIncreaseMemberViewCount(botUserId);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        assertAll(
                () -> assertThat(findMember).isNotNull(),
                () -> assertThat(findMember.getBotUserId()).isEqualTo(botUserId),
                () -> assertThat(findMember.getVisitCount()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("기존 회원이면 방문횟수를 1증가한다.")
    void increaseMemberViewCount() {
        // given
        String botUserId = "1";
        Member member = Member.create(botUserId, 1L);
        memberRepository.save(member);

        // when
        memberService.registerMemberOrIncreaseMemberViewCount(botUserId);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        assertThat(findMember.getVisitCount()).isEqualTo(2L);
    }
}