package seaung.uoscafeteriamenu.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class RedisCacheManagerConfig {

    /**
     Redis Cache 적용을 위한 RedisCacheManager 설정
     */

    private final RedisCacheConfig redisRepositoryConfig;

    @Bean
    public CacheManager redisCacheManager() {
        log.info("redisCacheManager...");
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofHours(12));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisRepositoryConfig.redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

}
