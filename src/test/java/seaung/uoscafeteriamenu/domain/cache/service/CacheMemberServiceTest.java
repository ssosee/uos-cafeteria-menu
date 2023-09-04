package seaung.uoscafeteriamenu.domain.cache.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

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
}