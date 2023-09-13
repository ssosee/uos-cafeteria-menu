package seaung.uoscafeteriamenu.global.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheMember;
import seaung.uoscafeteriamenu.domain.cache.service.CacheMemberService;
import seaung.uoscafeteriamenu.domain.service.MemberService;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

/**
 * <a href="https://kim-jong-hyun.tistory.com/143">참고</a>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ControllerAspect {

    private final TimeProvider timeProvider;
    private final MemberService memberService;

    @Around("seaung.uoscafeteriamenu.global.log.AppPointCuts.allController() && args(skillPayload, ..)")
    public Object doControllerCommonLogic(ProceedingJoinPoint joinPoint, SkillPayload skillPayload) throws Throwable {
        try {
            log.info("[LOG]={}", joinPoint.getSignature());

            // 회원 조회 하고 방문횟수 증가 / 회원이 없으면 회원 생성
            String botUserId = skillPayload.getUserRequest().getUser().getId();
            memberService.findCacheMemberOrSaveMemberInDatabaseAndRedis(botUserId);

            // 주말 확인
            checkWeekend();

        } catch (Exception e) {
            log.error("[LOG]={}", joinPoint.getSignature(), e);
            throw e;
        }

        return joinPoint.proceed();
    }


    // 주말 확인
    private void checkWeekend() {
        if(TimeProvider.isWeekend(timeProvider.getCurrentLocalDateTime())) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.NOT_PROVIDE_MENU_AT_WEEKEND);
        }
    }
}


