package seaung.uoscafeteriamenu.global.ratelimter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import lombok.Getter;

import java.time.Duration;

@Getter
public enum RatePlan {
    PROD {
        Bandwidth getLimit() {
          return Bandwidth.classic(5,
                  Refill.intervally(5, Duration.ofSeconds(1))
          );
      }
    },
    LOCAL {
        Bandwidth getLimit() {
            return Bandwidth.classic(100,
                    Refill.intervally(100, Duration.ofSeconds(1))
            );
        }
    };

    public static RatePlan resolvePlan(String apikey) {
        if(apikey.endsWith("admin")) {
            return PROD;
        }
        return LOCAL;
    }
}
