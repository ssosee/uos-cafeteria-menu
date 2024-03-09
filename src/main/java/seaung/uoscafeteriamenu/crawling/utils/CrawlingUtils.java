package seaung.uoscafeteriamenu.crawling.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

public class CrawlingUtils {

    public static final String NOT_PROVIDED_MENU = "학교에서 메뉴를 제공하지 않았다.. 휴먼.";

    private CrawlingUtils() {
    }

    public static String toDateString(LocalDateTime localDateTime) {
        // DateTimeFormatter를 사용하여 원하는 형식으로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d (EEE)", Locale.KOREA);

        return localDateTime.format(formatter);
    }

    public static boolean hasMenu(String menu) {
        if(StringUtils.hasText(menu) && menu.length() > 50) {
            return true;
        }
        return false;
    }

    public static MealType localDateTimeToMealType(LocalDateTime localDateTime) {
        LocalTime localTime = localDateTime.toLocalTime();
        // 아침(8:30 ~ 10:59)
        if(localTime.isAfter(LocalTime.of(8, 30)) && localTime.isBefore(LocalTime.of(11, 0))) {
            return MealType.BREAKFAST;
        }
        // 점심(11:00 ~ 13:59)
        else if(localTime.equals(LocalTime.of(11, 0)) ||
                (localTime.isAfter(LocalTime.of(11, 1)) && localTime.isBefore(LocalTime.of(14, 0)))) {
            return MealType.LUNCH;
        }
        // 저녁(14:00 ~ 18:29)
        return MealType.DINNER;
    }

    public static UosRestaurantName toUosRestaurantName(String restaurantName) {
        return UosRestaurantName.fromKrName(restaurantName);
    }

    public static String applyPatternToMenuDesc(String menuDesc) {

        String[] split = menuDesc.split("\n");

        StringBuilder sb = new StringBuilder();
        Pattern englishPatternWithRoundBrackets = Pattern.compile("^\\(.*[a-zA-Z].*\\)$");
        Pattern patternWithSpace = Pattern.compile("[a-zA-Z\\s]+");
        Pattern patternWithKcalAndSlashAndG = Pattern.compile("\\d+kcal/\\d+g");
        Pattern patternWithKcalAndSlashAndKcal = Pattern.compile("\\d+kcal / \\d+kcal");
        Pattern patternWithKcal = Pattern.compile("\\d+kcal");

        for(String word : split) {
            // "(Chicken Mayo Rice Bowl)" 가 아니고
            // "Chicken Mayo Rice Bowl" 가 아니고
            // "378kcal / 422kcal" 가 아니고
            // "678kcal" 가 아니고
            // "933kcal/43g" 가 아니면
            if(!word.matches(englishPatternWithRoundBrackets.pattern())
                    && !word.matches(patternWithSpace.pattern())
                    && !word.matches(patternWithKcalAndSlashAndG.pattern())
                    && !word.matches(patternWithKcalAndSlashAndKcal.pattern())
                    && !word.matches(patternWithKcal.pattern())) {
                sb.append(word).append("\n");
            }
        }

        return sb.toString().replaceAll("\n\n\n\n\n\n", "\n\n");
    }
}
