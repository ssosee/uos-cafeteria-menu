package seaung.uoscafeteriamenu.global.provider;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 시간을 고정하여 테스트하기 위해 사용
 */
public class TimeProvider {

    public LocalDateTime getCurrentLocalDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public static boolean isWeekend(LocalDateTime now) {
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
