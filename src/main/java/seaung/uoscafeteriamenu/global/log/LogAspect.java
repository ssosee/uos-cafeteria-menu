package seaung.uoscafeteriamenu.global.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final TimeProvider timeProvider;

    @Around("seaung.uoscafeteriamenu.global.log.AppPointCuts.allController()")
    public Object doLogInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[LOG]={}", joinPoint.getSignature());
            checkWeekend();
        } catch (Exception e) {
            log.error("[LOG]={}", joinPoint.getSignature(), e);
            throw e;
        }

        return joinPoint.proceed();
    }

    private void checkWeekend() {
        if(TimeProvider.isWeekend(timeProvider.getCurrentLocalDateTime())) {
            throw new UosRestaurantMenuException(UosRestaurantMenuException.NOT_PROVIDE_MENU_AT_WEEKEND);
        }
    }
}


