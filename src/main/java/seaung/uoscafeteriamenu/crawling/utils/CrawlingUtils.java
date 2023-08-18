package seaung.uoscafeteriamenu.crawling.utils;

import org.springframework.util.StringUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static MealType localDateTimeToMealType(LocalDateTime localDateTime) {
        LocalTime localTime = localDateTime.toLocalTime();
        // 아침(6:30 ~ 11:00)
        if(localTime.isAfter(LocalTime.of(6, 30)) && localTime.isBefore(LocalTime.of(11, 0))) {
            return MealType.BREAKFAST;
        }
        // 점심(11:00 ~ 14:00)
        else if(localTime.equals(LocalTime.of(11, 0)) ||
                (localTime.isAfter(LocalTime.of(11, 1)) && localTime.isBefore(LocalTime.of(14, 0)))) {
            return MealType.LUNCH;
        }
        // 저녁
        return MealType.DINNER;
    }

    public static UosRestaurantName toUosRestaurantName(String restaurantName) {
        return UosRestaurantName.fromKrName(restaurantName);
    }
}
