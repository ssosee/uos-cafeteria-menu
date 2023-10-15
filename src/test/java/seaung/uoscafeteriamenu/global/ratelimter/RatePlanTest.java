package seaung.uoscafeteriamenu.global.ratelimter;

import io.github.bucket4j.Bandwidth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class RatePlanTest {

    @Test
    @DisplayName("설정한 처리율 제한 장치 정책에 알맞는 정책을 반환한다.")
    void resolvePlan() {
        // given
        String test = RatePlan.TEST.getLevel();
        String local = RatePlan.LOCAL.getLevel();
        String dev = RatePlan.DEV.getLevel();
        String prod = RatePlan.PROD.getLevel();

        // when
        Bandwidth testBandwidth = RatePlan.resolvePlan(test);
        Bandwidth Localbandwidth = RatePlan.resolvePlan(local);
        Bandwidth Devbandwidth = RatePlan.resolvePlan(dev);
        Bandwidth Prodbandwidth = RatePlan.resolvePlan(prod);

        // then
        assertAll(
                () -> assertThat(testBandwidth).isEqualTo(RatePlan.TEST.getLimit()),
                () -> assertThat(Localbandwidth).isEqualTo(RatePlan.LOCAL.getLimit()),
                () -> assertThat(Devbandwidth).isEqualTo(RatePlan.DEV.getLimit()),
                () -> assertThat(Prodbandwidth).isEqualTo(RatePlan.PROD.getLimit())
        );
    }
}