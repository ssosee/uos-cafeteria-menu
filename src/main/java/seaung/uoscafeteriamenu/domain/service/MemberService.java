package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheMemberRepository;
import seaung.uoscafeteriamenu.domain.cache.service.CacheMemberService;
import seaung.uoscafeteriamenu.domain.entity.Member;
import seaung.uoscafeteriamenu.domain.repository.MemberBulkInsertRepository;
import seaung.uoscafeteriamenu.domain.repository.MemberRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CacheMemberRepository cacheMemberRepository;
    private final MemberBulkInsertRepository memberBulkInsertRepository;
    private final CacheMemberService cacheMemberService;

    @Deprecated
    public void registerMemberOrIncreaseMemberViewCount(String botUserId) {
        // 캐시에 회원이 존재하면 방문횟수 증가
        if(isCacheMemberInCacheAndIncreaseVisitCount(botUserId)) return;
        // 회원을 생성하고 DB와 cache에 저장
        createMemberInDatabaseAndCache(botUserId);
    }

    @Deprecated
    private boolean isCacheMemberInCacheAndIncreaseVisitCount(String botUserId) {
        // 캐시에서 회원 조회
        Optional<CacheMember> findCacheMember = cacheMemberRepository.findById(botUserId);

        // 캐시에 회원이 존재하면 방문횟수 증가
        if(findCacheMember.isPresent()) {
            CacheMember cacheMember = findCacheMember.get();
            cacheMember.increaseVisitCount();
            cacheMemberRepository.save(cacheMember);
            return true;
        }
        return false;
    }

    @Deprecated
    public CacheMember createMemberInDatabaseAndCache(String botUserId) {
        // DB에서 회원 조회
        Optional<Member> findMember = memberRepository.findByBotUserId(botUserId);

        // 회원이 없으면 회원을 생성하고 DB와 cache에 저장
        if(findMember.isEmpty()) {
            log.info("회원이 없으면 회원을 생성하고 DB와 cache에 저장");
            Member newMember = Member.create(botUserId, 1L);
            memberRepository.save(newMember);

            CacheMember cacheMember = CacheMember.of(newMember);
            cacheMemberRepository.save(cacheMember);
        }

        return CacheMember.of(findMember.get());
    }

    @Transactional
    public void syncCacheMemberVisitCountToDatabaseMember() {
        // 모든 Member 캐시에 있는 데이터를 데이터베이스로 동기화한다.
        Iterable<CacheMember> cacheMembers = cacheMemberRepository.findAll();
        Map<String, Member> cacheMembersToMemberMap = StreamSupport.stream(cacheMembers.spliterator(), false)
                .collect(Collectors.toMap(CacheMember::getBotUserId, cacheMember ->
                        Member.create(cacheMember.getBotUserId(), cacheMember.getVisitCount())
                ));

        List<String> botUserIdsInCacheMember = new ArrayList<>(cacheMembersToMemberMap.keySet());

        List<Member> members = memberRepository.findByBotUserIdIn(botUserIdsInCacheMember);
        for(Member m : members) {
            Member member = cacheMembersToMemberMap.get(m.getBotUserId());
            m.changeVisitCount(member.getVisitCount());
        }

        List<String> botUserIds = members.stream()
                .map(Member::getBotUserId)
                .collect(Collectors.toList());

        Set<Member> memberSet = new HashSet<>();
        for(String botUserId : botUserIdsInCacheMember) {
            if(!botUserIds.contains(botUserId)) {
                Member findMember = cacheMembersToMemberMap.get(botUserId);
                Member member = Member.create(botUserId, findMember.getVisitCount());

                memberSet.add(member);
            }
        }

        memberBulkInsertRepository.saveAll(memberSet);
        //memberRepository.saveAll(memberSet);
    }

    @Deprecated
    @Transactional
    public void syncCacheMemberVisitCountToDatabaseMember2() {
        // 모든 Member 캐시에 있는 데이터를 데이터베이스로 동기화한다.
        List<CacheMember> cacheMembers = cacheMemberService.findAllCacheMembers();

        Map<String, Member> cacheMembersToMemberMap = cacheMembers.stream()
                .collect(Collectors.toMap(CacheMember::getBotUserId, Member::of));

        List<String> botUserIds = cacheMembers.stream()
                .map(CacheMember::getBotUserId)
                .collect(Collectors.toList());

        List<Member> members = memberRepository.findByBotUserIdIn(botUserIds);
        for(Member m : members) {
            Member member = cacheMembersToMemberMap.get(m.getBotUserId());
            m.changeVisitCount(member.getVisitCount());
        }
    }
}
