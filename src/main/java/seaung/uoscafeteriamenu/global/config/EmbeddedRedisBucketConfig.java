package seaung.uoscafeteriamenu.global.config;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import seaung.uoscafeteriamenu.global.ratelimter.RatePlan;

import java.time.Duration;

@Slf4j
@Configuration
@Profile({"test", "local"})
public class EmbeddedRedisBucketConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${bucket4j.level}")
    private String bucketLevel;

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create(RedisURI.builder()
                .withHost(redisHost)
                .withPort(redisPort)
                .build());
    }

    @Bean
    public LettuceBasedProxyManager lettuceBasedProxyManager() {
        return LettuceBasedProxyManager
                .builderFor(redisClient())
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(1)))
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        log.info("처리율 제한 자치 레벨 설정={}", bucketLevel);
        return BucketConfiguration.builder()
                .addLimit(RatePlan.resolvePlan(bucketLevel))
                .build();
    }
}
