package seaung.uoscafeteriamenu.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class CrawlingDateUtilsTest {

    @Test
    @DisplayName("LocalDateTime을 '월/일 (요일)'로 변환한다.")
    void CrawlingDateUtilsToString() {
        // given
        // 2023-08-09 00:00
        LocalDateTime now = LocalDateTime.of(2023, Month.AUGUST, 9, 0 ,0);

        // when
        String result = CrawlingDateUtils.toString(now.minusDays(5));

        // then
        assertThat(result).isEqualTo("8/4 (수)");
    }
}