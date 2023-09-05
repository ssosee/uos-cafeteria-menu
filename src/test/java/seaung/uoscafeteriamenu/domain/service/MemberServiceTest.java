package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
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
    @Autowired
    CacheManager cacheManager;

    @AfterEach
    void tearDown() {
        cacheManager.getCacheNames()
                .forEach(name -> cacheManager.getCache(name).clear());
        cacheMemberRepository.deleteAll();
    }

    @Test
    @DisplayName("캐시에 있는 회원의 방문 횟수를 데이터베이스와 동기화 한다.")
    void syncCacheMemberVisitCountToDatabaseMember() {
        // given
        String botUserId = "1";

        CacheMember cacheMember = CacheMember.create(botUserId, 1L, 100L, 3600);
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