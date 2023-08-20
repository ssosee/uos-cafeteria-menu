package seaung.uoscafeteriamenu.domain.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;

public enum OperatingTime {
    OPEN,
    CLOSED;

    public static boolean isOperatingTime(LocalDateTime now) {
        LocalTime localTime = now.toLocalTime();
        // 6:30 ~ 18:30
        if(localTime.equals(LocalTime.of(6, 30)) ||
                (localTime.isAfter(LocalTime.of(6, 30)) && localTime.isBefore(LocalTime.of(18,30)))) {
            return true;
        }
        return false;
    }

    public static OperatingTime toOperatingTime(LocalDateTime now) {
        LocalTime localTime = now.toLocalTime();
        // 6:30 ~ 18:30
        if(localTime.equals(LocalTime.of(6, 30)) ||
                (localTime.isAfter(LocalTime.of(6, 30)) && localTime.isBefore(LocalTime.of(18,30)))) {
            return OPEN;
        }
        return CLOSED;
    }
}
