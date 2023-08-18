package seaung.uoscafeteriamenu.global.provider;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class TimeProviderTest {

    @Test
    @DisplayName("주말이면 true를 반환한다.")
    void isWeekend() {
        // given
        LocalDateTime sat = LocalDateTime.of(2023, 8, 19, 0, 0);
        LocalDateTime sun = LocalDateTime.of(2023, 8, 20, 0, 0);

        // when
        boolean isWeekendSat = TimeProvider.isWeekend(sat);
        boolean isWeekendSun = TimeProvider.isWeekend(sun);

        // then
        assertAll(
                () -> assertThat(isWeekendSat).isTrue(),
                () -> assertThat(isWeekendSun).isTrue()
        );
    }

    @Test
    @DisplayName("평일이면 false를 반환한다.")
    void isWeekDay() {
        // given
        LocalDateTime mon = LocalDateTime.of(2023, 8, 14, 0, 0);
        LocalDateTime tue = LocalDateTime.of(2023, 8, 15, 0, 0);
        LocalDateTime wed = LocalDateTime.of(2023, 8, 16, 0, 0);
        LocalDateTime thr = LocalDateTime.of(2023, 8, 17, 0, 0);
        LocalDateTime fri = LocalDateTime.of(2023, 8, 18, 0, 0);

        // when
        boolean isWeekendMon = TimeProvider.isWeekend(mon);
        boolean isWeekendTue = TimeProvider.isWeekend(tue);
        boolean isWeekendWed = TimeProvider.isWeekend(wed);
        boolean isWeekendThr = TimeProvider.isWeekend(thr);
        boolean isWeekendFri = TimeProvider.isWeekend(fri);

        // then
        assertAll(
                () -> assertThat(isWeekendMon).isFalse(),
                () -> assertThat(isWeekendTue).isFalse(),
                () -> assertThat(isWeekendWed).isFalse(),
                () -> assertThat(isWeekendThr).isFalse(),
                () -> assertThat(isWeekendFri).isFalse()
        );
    }
}