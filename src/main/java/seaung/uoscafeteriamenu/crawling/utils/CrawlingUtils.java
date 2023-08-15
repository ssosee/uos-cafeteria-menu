package seaung.uoscafeteriamenu.crawling.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CrawlingUtils {

    public static final String NOT_PROVIDED_MENU = "학교에서 메뉴를 제공하지 않았다.. 휴먼.";

    public static String toDateString(LocalDateTime localDateTime) {
        // DateTimeFormatter를 사용하여 원하는 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d (EEE)", Locale.KOREA);
        return localDateTime.format(formatter);
    }

    public static boolean hasMenu(String menu) {
        if(StringUtils.hasText(menu) && menu.length() > 25) {
            return true;
        }
        return false;
    }
}
