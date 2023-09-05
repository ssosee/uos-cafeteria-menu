package seaung.uoscafeteriamenu.domain.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.entity.Member;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class MemberBulkInsertRepositoryTest {

    @Autowired
    MemberBulkInsertRepository memberBulkInsertRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CacheMemberRepository cacheMemberRepository;
    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("1,000개의 데이터 벌크 insert")
    void memberRepositorySaveAll() {
        // given
        Set<Member> members = new HashSet<>();
        Set<CacheMember> cacheMembers = new HashSet<>();
        for(int i = 0; i < 1000; i++) {
            Member member = Member.create(UUID.randomUUID().toString(), 0L);
            members.add(member);
            cacheMembers.add(CacheMember.of(member));
        }

        // when
        long startTime = System.currentTimeMillis();
        cacheMemberRepository.saveAll(cacheMembers);
        long endTime = System.currentTimeMillis();
        long cacheTime = endTime - startTime;

        startTime = System.currentTimeMillis();
        memberRepository.saveAll(members);
        endTime = System.currentTimeMillis();
        long notBulkTime = endTime - startTime;

        memberRepository.deleteAll();

        startTime = System.currentTimeMillis();
        memberBulkInsertRepository.saveAll(members);
        endTime = System.currentTimeMillis();
        long bulkTime = endTime - startTime;

        startTime = System.currentTimeMillis();
        memberRepository.findAll();
        endTime = System.currentTimeMillis();
        long findInDatabaseTime = endTime - startTime;

        startTime = System.currentTimeMillis();
        cacheMemberRepository.findAll();
        endTime = System.currentTimeMillis();
        long findInRedisTime = endTime - startTime;

        // then
        System.out.println("[Write] jpa 사용="+notBulkTime+", jdbc 사용"+bulkTime+", redis 사용"+cacheTime);
        System.out.println("[Read] jpa 사용="+findInDatabaseTime+", redis 사용"+findInRedisTime);
        assertThat(notBulkTime).isGreaterThan(bulkTime);
    }

    @Test
    @DisplayName("JdbcTemplate를 사용하여 1,000개의 데이터 벌크 insert")
    void memberBulkInsertRepositorySaveAll() {
        // given
        Set<Member> members = new HashSet<>();
        for(int i = 0; i < 1000; i++) {
            Member member = Member.create(UUID.randomUUID().toString(), 0L);
            members.add(member);
        }

        // when
        long startTime = System.currentTimeMillis();
        memberBulkInsertRepository.saveAll(members);
        long endTime = System.currentTimeMillis();

        // then
        System.out.println("걸린시간="+(endTime - startTime));
    }
}