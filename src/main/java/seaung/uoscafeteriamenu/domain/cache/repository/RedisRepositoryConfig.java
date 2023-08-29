package seaung.uoscafeteriamenu.domain.cache.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    /**
     * RedisConnectionFactory를 통해 내장 혹은 외부의 Redis를 연결합니다.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("RedisConnectionFactory 실행");
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * RedisTemplate을 통해 RedisConnection에서 넘겨준 byte 값을 객체 직렬화합니다.
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        log.info("RedisTemplate 실행");
        RedisTemplate<byte[],byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

}
