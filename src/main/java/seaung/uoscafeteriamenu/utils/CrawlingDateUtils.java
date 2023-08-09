package seaung.uoscafeteriamenu.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CrawlingDateUtils {
    public static String toString(LocalDateTime localDateTime) {
        // DateTimeFormatter를 사용하여 원하는 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d (EEE)", Locale.KOREA);
        return localDateTime.format(formatter);
    }
}
