package seaung.uoscafeteriamenu.global.ratelimter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import seaung.uoscafeteriamenu.web.exception.RateLimiterException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class BucketResolverTest {

    @Autowired
    BucketResolver bucketResolver;

    @Autowired
    CacheManager cacheManager;

    @AfterEach
    void tearDown() {
        cacheManager.getCacheNames()
                .forEach(name -> cacheManager.getCache(name).clear());
    }

    @Test
    @DisplayName("처리율 제한 장치에 제한이 걸리지 않으면 true를 반환한다.")
    void checkBucketCounter() {
        // given
        String key = "1";
        // when
        boolean result = bucketResolver.checkBucketCounter(key);
        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("처리율 제한 장치에 제한이 걸리면 예외가 발생한다.")
    void checkBucketCounterException() {
        // given
        String key = "1";

        // when
        for(int i = 0; i < 100; i++) {
            bucketResolver.checkBucketCounter(key);
        }

        // then
        assertThatThrownBy(() -> bucketResolver.checkBucketCounter(key))
                .isInstanceOf(RateLimiterException.class)
                .hasMessage(RateLimiterException.TOO_MANY_REQUEST);
    }

    @Test
    @DisplayName("key1는 처리율 제한이 걸리지 않고, key2은 처리율 제한이 걸려 예외가 발생한다.(처리율 제한 장치는 key에 따라서 처리율 제한을 계산한다.)")
    void checkBucketCounterKey1NotExceptionAndKey2Exception() {
        // given
        String key1 = "1";
        String key2 = "2";

        // when
        boolean key1Result = bucketResolver.checkBucketCounter(key1);
        for(int i = 0; i < 100; i++) {
            bucketResolver.checkBucketCounter(key2);
        }

        // then
        assertAll(
                () -> assertThat(key1Result).isTrue(),
                () -> assertThatThrownBy(() -> bucketResolver.checkBucketCounter(key2))
                        .isInstanceOf(RateLimiterException.class)
                        .hasMessage(RateLimiterException.TOO_MANY_REQUEST)
        );
    }
}