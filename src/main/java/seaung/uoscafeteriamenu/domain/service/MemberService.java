package seaung.uoscafeteriamenu.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final CacheMemberRepository cacheMemberRepository;
    private final MemberBulkInsertRepository memberBulkInsertRepository;

    @Transactional
    public CacheMember findCacheMemberOrSaveMemberInDatabaseAndRedis(String botUserId) {
        // 캐시에서 회원 조회
        CacheMember findCacheMember = cacheMemberRepository.findById(botUserId).orElse(null);
        // 캐시에 회원이 있으면
        if(!ObjectUtils.isEmpty(findCacheMember)) {
            // 방문횟수 증가
            findCacheMember.increaseVisitCount();
            return cacheMemberRepository.save(findCacheMember);
        }

        // 캐시에 회원이 없으면 회원을 생성하고 DB와 캐시에 저장
        Member member = Member.create(botUserId, 1L);
        memberRepository.save(member);

        return cacheMemberRepository.save(CacheMember.of(member));
    }


    @Transactional
    // 모든 Member 캐시에 있는 데이터를 데이터베이스로 동기화한다.
    public void syncCacheMemberVisitCountToDatabaseMember() {

        // 캐시에서 모든 회원 조회
        Iterable<CacheMember> cacheMembers = cacheMemberRepository.findAll();

        Map<String, Member> cacheMembersToMemberMap = getCacheMembersToMemberMap(cacheMembers);
        List<String> botUserIdsInCacheMember = new ArrayList<>(cacheMembersToMemberMap.keySet());

        // 회원 조회
        List<Member> members = memberRepository.findByBotUserIdIn(botUserIdsInCacheMember);
        for(Member m : members) {
            Member member = cacheMembersToMemberMap.get(m.getBotUserId());
            // 조회수 증가
            m.changeVisitCount(member.getVisitCount());
        }

        List<String> botUserIds = getBotUserIds(members);

        // 새로운 회원 저장(캐시에만 존재하는 회원을 조회)
        Set<Member> newMembers = getNewMembers(botUserIdsInCacheMember, botUserIds, cacheMembersToMemberMap);

        // 새로운 회원 벌크 삽입
        memberBulkInsertRepository.saveAll(newMembers);
        // 캐시 삭제
        cacheMemberRepository.deleteAll();
    }

    private Set<Member> getNewMembers(List<String> botUserIdsInCacheMember, List<String> botUserIds, Map<String, Member> cacheMembersToMemberMap) {
        Set<Member> memberSet = new HashSet<>();
        for(String botUserId : botUserIdsInCacheMember) {
            // 데이터베이스의 botUserIds가 캐시에 있는 botUserId를 포함하지 않으면
            if(!botUserIds.contains(botUserId)) {
                Member findMember = cacheMembersToMemberMap.get(botUserId);
                Member member = Member.create(botUserId, findMember.getVisitCount());

                memberSet.add(member);
            }
        }
        return memberSet;
    }

    private List<String> getBotUserIds(List<Member> members) {
        return members.stream()
                .map(Member::getBotUserId)
                .collect(Collectors.toList());
    }

    private Map<String, Member> getCacheMembersToMemberMap(Iterable<CacheMember> cacheMembers) {
        return StreamSupport.stream(cacheMembers.spliterator(), false)
                .collect(Collectors.toMap(CacheMember::getBotUserId,
                        cacheMember -> Member.create(cacheMember.getBotUserId(), cacheMember.getVisitCount())
                ));
    }
}
