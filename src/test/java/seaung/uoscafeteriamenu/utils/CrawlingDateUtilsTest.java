package seaung.uoscafeteriamenu.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;

class CrawlingDateUtilsTest {

    @Test
    @DisplayName("LocalDateTime을 '월/일 (요일)'로 변환한다.")
    void CrawlingDateUtilsToString() {
        // given
        // 2023-08-09 00:00
        LocalDateTime now = LocalDateTime.of(2023, Month.AUGUST, 9, 0 ,0);

        // when
        String result = CrawlingUtils.toDateString(now);

        // then
        assertThat(result).isEqualTo("8/9 (수)");
    }

    @Test
    @DisplayName("메뉴가 빈문자열이면 false를 반환한다.")
    void hasMenuIsBlank() {
        // given
        String menu = "";
        // when
        boolean result = CrawlingUtils.hasMenu(menu);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("메뉴의 글자수가 100보다 작으면 false를 반환한다.")
    void hasMenuIsLengthLess25() {
        // given
        String menu = "금일은 학교 사정상 운영하지 않습니다.";
        // when
        boolean result = CrawlingUtils.hasMenu(menu);
        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("오전 8시 30분 ~ 11시 사이에는 아침타입을 반환한다.")
    void localDateTimeToMealTypeIsBREAKFAST() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 10, 59, 59);

        // when
        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);

        // then
        assertThat(mealType).isEqualTo(MealType.BREAKFAST);
    }

    @Test
    @DisplayName("오전 11시 ~ 14시 사이에는 점심타입을 반환한다.")
    void localDateTimeToMealTypeIsLUNCH() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 11, 0, 0);

        // when
        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);

        // then
        assertThat(mealType).isEqualTo(MealType.LUNCH);
    }

    @Test
    @DisplayName("14시 ~ 6시30분 사이에는 저녁타입을 반환한다.")
    void localDateTimeToMealTypeIsDINNER() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 16, 14, 0, 0);

        // when
        MealType mealType = CrawlingUtils.localDateTimeToMealType(now);

        // then
        assertThat(mealType).isEqualTo(MealType.DINNER);
    }

    @Test
    @DisplayName("TextCard의 글자수가 최대 400자 이기 때문에 정규식을 적용하여 학식메뉴 글자수를 줄인다." +
            "(괄호를 포함한 영어, 공백을 포함한 영어, 칼로리 정보, 질량 정보를 제외한다.)")
    void applyPatternToMenuDesc() {
        // given
        String str = "학생회관 1층(조식)\n" +
                "\uD83D\uDC40 조회수: 1\n" +
                "\uD83D\uDC4D 추천수: 0\n\n" +
                "코너 A\n" +
                "11:00~14:00\n" +
                "라면(Ramen) 2,000원\n" +
                "(토핑:치즈,떡,만두,공기밥)\n" +
                "오징어해장라면 2,800원\n" +
                "삶은계란(steamed egg) 400원\n" +
                "\n" +
                "\n" +
                "코너 B\n" +
                "11:00~14:00\n" +
                "(3,800원)\n" +
                "참치마요덮밥 3,800원\n" +
                "(Tuna Mayo Rice Bowl)\n" +
                "햄마요덮밥 3,800원\n" +
                "(Ham Mayo Rice Bowl)\n" +
                "치킨마요덮밥 3,800원\n" +
                "(Chicken Mayo Rice Bowl)\n" +
                "\n" +
                "가다랑어:원양산\n" +
                "돈육:덴마크산,계육:국내산\n" +
                "\n" +
                "셀프코너\n" +
                "(모든 코너 이용 가능)\n" +
                "11:00~14:00\n" +
                "장국\n" +
                "콩나물무침\n" +
                "맛김치\n" +
                "\n" +
                "코너 C\n" +
                "11:00~14:00\n" +
                "(3,800원)\n" +
                "사골떡국 3,800원\n" +
                "rice cake soup\n" +
                "매콤콩나물불고기 3,800원\n" +
                "Bean Sprout Bulgogi\n" +
                "\n" +
                "소:국산,호주산, 돼지,닭:국산\n" +
                "325kcal / 473kcal\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "코너 E\n" +
                "11:00~13:30\n" +
                "(5,000원)\n" +
                "돼지갈비탕\n" +
                "어묵볶음\n" +
                "천사채샐러드\n" +
                "\n" +
                "가다랑어:원양산,새우:베트남산\n" +
                "454kcal";

        // when
        String result = CrawlingUtils.applyPatternToMenuDesc(str);

        // then
        assertThat(result).isEqualTo("학생회관 1층(조식)\n" +
                "\uD83D\uDC40 조회수: 1\n" +
                "\uD83D\uDC4D 추천수: 0\n" +
                "\n" +
                "코너 A\n" +
                "11:00~14:00\n" +
                "라면(Ramen) 2,000원\n" +
                "(토핑:치즈,떡,만두,공기밥)\n" +
                "오징어해장라면 2,800원\n" +
                "삶은계란(steamed egg) 400원\n" +
                "\n" +
                "\n" +
                "코너 B\n" +
                "11:00~14:00\n" +
                "(3,800원)\n" +
                "참치마요덮밥 3,800원\n" +
                "햄마요덮밥 3,800원\n" +
                "치킨마요덮밥 3,800원\n" +
                "\n" +
                "가다랑어:원양산\n" +
                "돈육:덴마크산,계육:국내산\n" +
                "\n" +
                "셀프코너\n" +
                "(모든 코너 이용 가능)\n" +
                "11:00~14:00\n" +
                "장국\n" +
                "콩나물무침\n" +
                "맛김치\n" +
                "\n" +
                "코너 C\n" +
                "11:00~14:00\n" +
                "(3,800원)\n" +
                "사골떡국 3,800원\n" +
                "매콤콩나물불고기 3,800원\n" +
                "\n" +
                "소:국산,호주산, 돼지,닭:국산\n" +
                "\n" +
                "코너 E\n" +
                "11:00~13:30\n" +
                "(5,000원)\n" +
                "돼지갈비탕\n" +
                "어묵볶음\n" +
                "천사채샐러드\n" +
                "\n" +
                "가다랑어:원양산,새우:베트남산" +
                "\n");
    }
}