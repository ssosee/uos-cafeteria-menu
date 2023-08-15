package seaung.uoscafeteriamenu.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;

import java.time.LocalDateTime;
import java.time.Month;

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
    @DisplayName("메뉴의 글자수가 25보다 작으면 false를 반환한다.")
    void hasMenuIsLengthLess25() {
        // given
        String menu = "금일은 학교 사정상 운영하지 않습니다.";
        // when
        boolean result = CrawlingUtils.hasMenu(menu);
        // then
        assertThat(result).isFalse();
    }
}