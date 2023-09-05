package seaung.uoscafeteriamenu.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 실제 학교 식당은 8:00에 open 이지만,
 * 조식의 경우 그전에 메뉴를 검색하는 상황이 발생하기 때문에 7:30으로 변경
 * (크롤링 시간은 매주 월요일 7:00)
 */
public enum OperatingTime {
    OPEN,
    CLOSED;

    public static boolean isOperatingTime(LocalDateTime now) {
        LocalTime localTime = now.toLocalTime();
        // 7:30 ~ 18:30
        if(localTime.equals(LocalTime.of(7, 30)) ||
                (localTime.isAfter(LocalTime.of(7, 30)) && localTime.isBefore(LocalTime.of(18,30)))) {
            return true;
        }
        return false;
    }

    public static OperatingTime toOperatingTime(LocalDateTime now) {
        LocalTime localTime = now.toLocalTime();
        // 7:30 ~ 18:30
        if(localTime.equals(LocalTime.of(7, 30)) ||
                (localTime.isAfter(LocalTime.of(7, 30)) && localTime.isBefore(LocalTime.of(18,30)))) {
            return OPEN;
        }
        return CLOSED;
    }
}
