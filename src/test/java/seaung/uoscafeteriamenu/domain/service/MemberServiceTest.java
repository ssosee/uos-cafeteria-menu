package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    RedisCacheManager cacheManager;
    @Autowired
    RedisTemplate<?, ?> redisTemplate;

    @AfterEach
    void tearDown() {
        cacheManager.getCacheNames()
                .forEach(name -> cacheManager.getCache(name).clear());
        cacheMemberRepository.deleteAll();
    }

    @Test
    @DisplayName("캐시에서 회원을 조회하고 캐시에 회원이 없으면 데이터베이스와 캐시에 회원을 생성한다.")
    void findCacheMemberOrSaveMemberInDatabaseAndRedis() {
        // given
        String botUserId = "1";

        // when
        CacheMember cacheMember = memberService.findCacheMemberOrSaveMemberInDatabaseAndRedis(botUserId);

        // then
        Member findMember = memberRepository.findByBotUserId(botUserId).get();
        CacheMember findCacheMember = cacheMemberRepository.findById(botUserId).get();

        assertAll(
                () -> assertThat(cacheMember.getBotUserId()).isEqualTo("1"),
                () -> assertThat(findCacheMember.getBotUserId()).isEqualTo("1"),
                () -> assertThat(findMember.getBotUserId()).isEqualTo("1")
        );
    }

    @Test
    @DisplayName("캐시에 있는 회원의 방문 횟수를 데이터베이스와 동기화 하고, 캐시에 있는 회원을 삭제한다.")
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

        Iterable<CacheMember> cacheMembers = cacheMemberRepository.findAll();
        assertThat(cacheMembers).isEmpty();
    }
}