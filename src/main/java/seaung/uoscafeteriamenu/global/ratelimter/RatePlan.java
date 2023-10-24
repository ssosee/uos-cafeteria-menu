package seaung.uoscafeteriamenu.global.ratelimter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Getter
@RequiredArgsConstructor
public enum RatePlan {
    TEST("test") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)));
        }
    },
    LOCAL("local") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(9999, Refill.intervally(9999, Duration.ofSeconds(1)));
        }
    },
    DEV("dev") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofSeconds(1)));
        }
    },
    PROD("prod") {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(1, Refill.intervally(1, Duration.ofSeconds(1)));
        }
    };

    private final String level;

    public abstract Bandwidth getLimit();

    public static Bandwidth resolvePlan(String targetLevel) {
        if (PROD.getLevel().equals(targetLevel)) return PROD.getLimit();
        else if(DEV.getLevel().equals(targetLevel)) return DEV.getLimit();
        else if(LOCAL.getLevel().equals(targetLevel)) return LOCAL.getLimit();
        return TEST.getLimit();
    }
}
