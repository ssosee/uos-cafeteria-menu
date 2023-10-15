package seaung.uoscafeteriamenu.domain.cache.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CacheMemberServiceTest {

    @Autowired
    CacheMemberService cacheMemberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CacheMemberRepository cacheMemberRepository;
    @Autowired
    CacheManager cacheManager;

    @Autowired
    RedisTemplate<?, ?> redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("회원이 없으면 데이터베이스에 회원을 생성하고 캐시에 저장한다.")
    void findMemberOrCreateMemberByBotUserId() {
        // given
        String botUserId = "1";

        // when
        cacheMemberService.findMemberOrCreateMemberByBotUserId(botUserId);

        // then
        // 캐시에 데이터가 있는지 확인
        CacheMember findCacheMember = cacheManager.getCache("cacheMember").get(botUserId, CacheMember.class);
        assertThat(findCacheMember).isNotNull();

        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        assertThat(findMember).isNotNull();
    }

    @Test
    @DisplayName("캐시에 회원을 저장하고, 방문횟수를 1증가 한다.")
    void increaseCacheMemberVisitCount() {
        // given
        String botUserId = "1";
        Member member = Member.create(botUserId, 0L);
        CacheMember cacheMember = CacheMember.of(member);

        // when
        cacheMemberService.increaseCacheMemberVisitCount(cacheMember);

        // then
        CacheMember findCacheMember = cacheManager.getCache("cacheMember").get(botUserId, CacheMember.class);
        assertThat(findCacheMember).isNotNull();
        assertThat(findCacheMember.getVisitCount()).isEqualTo(1L);
    }
}