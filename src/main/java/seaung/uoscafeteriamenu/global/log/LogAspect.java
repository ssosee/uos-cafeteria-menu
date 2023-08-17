package seaung.uoscafeteriamenu.global.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("seaung.uoscafeteriamenu.global.log.AppPointCuts.allController()")
    public Object doLogInfo(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[LOG]={}", joinPoint.getSignature());
        } catch (Exception e) {
            log.error("[LOG]={}", joinPoint.getSignature(), e);
        }
        return joinPoint.proceed();
    }
}


