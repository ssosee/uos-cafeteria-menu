package seaung.uoscafeteriamenu.domain.cache.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CacheMemberServiceTest {

    @Autowired
    CacheMemberService cacheMemberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("")
    void test1() {
        // given

        // when
        CacheMember cacheMember1 = cacheMemberService.findMemberOrCreateMemberByBotUserId("1");
        CacheMember cacheMember2 = cacheMemberService.findMemberOrCreateMemberByBotUserId("1");
        CacheMember cacheMember3 = cacheMemberService.findMemberOrCreateMemberByBotUserId("1");

        // then
        assertThat(cacheMember1.getVisitCount()).isEqualTo(1L);
        assertThat(cacheMember2.getVisitCount()).isEqualTo(2L);
        assertThat(cacheMember3.getVisitCount()).isEqualTo(3L);
    }

    @Test
    @DisplayName("")
    @Commit
    void test() {
        // given
        String botUserId = "1";
        Member member = Member.create(botUserId, 0L);
        memberRepository.save(member);

        // when
        CacheMember cacheMember1 = cacheMemberService.findCacheMember("1");
        cacheMemberService.increaseCacheMemberVisitCount("1");

        CacheMember cacheMember2 = cacheMemberService.findCacheMember("1");

        System.out.println(cacheMember1);
        System.out.println(cacheMember2);
        // then
        assertThat(cacheMember1.getVisitCount()).isEqualTo(0);
        assertThat(cacheMember2.getVisitCount()).isEqualTo(1);
    }
}