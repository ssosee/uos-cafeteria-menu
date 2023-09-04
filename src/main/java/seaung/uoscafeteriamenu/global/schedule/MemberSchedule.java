package seaung.uoscafeteriamenu.global.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.service.MemberService;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberSchedule {

    private final MemberService memberService;

    // 30분에 한 번씩 동기화
    //@Scheduled(cron = "0 0/30 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public void syncCacheMemberVisitCountToDatabaseMemberSchedule() {
        log.info("회원 방문 횟수 동기화..");
        memberService.syncCacheMemberVisitCountToDatabaseMember();
        //memberService.syncCacheMemberVisitCountToDatabaseMember2();
    }
}
