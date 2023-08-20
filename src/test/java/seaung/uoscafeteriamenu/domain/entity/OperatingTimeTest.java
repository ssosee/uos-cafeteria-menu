package seaung.uoscafeteriamenu.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class OperatingTimeTest {

    @Test
    @DisplayName("현재 시간이 8:30 ~ 18:29 이면 운영시간 이다.")
    void isOperatingTimeTure() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 20, 8, 30, 0);

        // when
        boolean result = OperatingTime.isOperatingTime(now);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("현재 시간이 8:30 ~ 18:29 이 아니면 운영시간이 아니다.")
    void isOperatingTimeFalse() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 20, 18, 30, 0);

        // when
        boolean result = OperatingTime.isOperatingTime(now);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("현재 시간이 8:30 ~ 18:29 이면 운영시간 이다.")
    void toOperatingTimeOPEN() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 20, 8, 30, 0);

        // when
        OperatingTime result = OperatingTime.toOperatingTime(now);

        // then
        assertThat(result).isEqualTo(OperatingTime.OPEN);
    }

    @Test
    @DisplayName("현재 시간이 6:30 ~ 18:29 이 아니면 운영시간이 아니다.")
    void toOperatingTimeCLOSED() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 20, 18, 30, 0);

        // when
        OperatingTime result = OperatingTime.toOperatingTime(now);

        // then
        assertThat(result).isEqualTo(OperatingTime.CLOSED);
    }
}