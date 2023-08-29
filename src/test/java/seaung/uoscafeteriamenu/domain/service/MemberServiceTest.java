package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class MemberServiceTest {
    private static final String REDIS_DOCKER_IMAGE = "redis:latest";
    static {
        GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                .withExposedPorts(6379)
                .withReuse(true);
        REDIS_CONTAINER.start();

        // (3)
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CacheMemberRepository cacheMemberRepository;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원이 없으면 DB에 회원을 저장하고 cache에도 저장한다.")
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