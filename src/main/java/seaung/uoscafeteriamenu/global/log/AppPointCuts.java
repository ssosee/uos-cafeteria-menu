package seaung.uoscafeteriamenu.global.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;

public class AppPointCuts {

    // 클래스 패턴 이름이 *Controller
    @Pointcut("execution(* *..*Controller.*(..))")
    public void allController() {}

    // 클래스 패턴 이름이 *ControllerAdvice
    @Pointcut("execution(* *..*ControllerAdvice.*(..))")
    public void allControllerAdvice() {}

    // 클래스 패턴 이름이 *Schedule
    @Pointcut("execution(* *..*Schedule.*(..))")
    public void allSchedule() {}

}
