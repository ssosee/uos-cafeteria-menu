package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class MemberServiceTest {
//    private static final String REDIS_DOCKER_IMAGE = "redis:latest";
//    static {
//        GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
//                .withExposedPorts(6379)
//                .withReuse(true);
//        REDIS_CONTAINER.start();
//
//        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
//        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
//    }

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CacheMemberRepository cacheMemberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("캐시, 데이터베이스에 회원이 없으면 데이터베이스와 캐시에 회원을 저장한다.")
    void registerMember() {
        // given
        String botUserId = "1";

        // when
        memberService.registerMemberOrIncreaseMemberViewCount(botUserId);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        CacheMember cacheMember = cacheMemberRepository.findById(botUserId).get();
        assertAll(
                () -> assertThat(findMember).isNotNull(),
                () -> assertThat(findMember.getBotUserId()).isEqualTo(botUserId),
                () -> assertThat(findMember.getVisitCount()).isEqualTo(1L),
                () -> assertThat(cacheMember).isNotNull(),
                () -> assertThat(cacheMember.getBotUserId()).isEqualTo(botUserId),
                () -> assertThat(cacheMember.getVisitCount()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("캐시에 회원이 존재하면 캐시에 있는 회원의 방문횟수를 1증가한다.")
    void increaseMemberViewCount() {
        // given
        String botUserId = "1";

        CacheMember cacheMember = CacheMember.create(botUserId, 1L, 3600);
        cacheMemberRepository.save(cacheMember);

        // when
        memberService.registerMemberOrIncreaseMemberViewCount(botUserId);

        // then
        CacheMember findCacheMember = cacheMemberRepository.findById(botUserId).get();
        assertThat(findCacheMember.getVisitCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("캐시에 있는 회원 방문횟수를 데이터베이스에 동기화 한다.")
    void syncCacheMemberVisitCountToDatabaseMember() {
        // given
        String botUserId = "1";

        CacheMember cacheMember = CacheMember.create(botUserId, 100L, 3600);
        cacheMemberRepository.save(cacheMember);

        Member member = Member.create(botUserId, 1L);
        memberRepository.save(member);

        // when
        memberService.syncCacheMemberVisitCountToDatabaseMember();

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        assertThat(findMember.getVisitCount()).isEqualTo(100L);
    }
}