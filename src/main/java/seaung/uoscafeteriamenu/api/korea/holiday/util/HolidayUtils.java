package seaung.uoscafeteriamenu.api.korea.holiday.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HolidayUtils {

    private HolidayUtils() {
    }

    public static String toDateString(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA);
        return localDateTime.format(formatter);
    }
}
