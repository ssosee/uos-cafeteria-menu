package seaung.uoscafeteriamenu.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ObjectUtils;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * <a href="https://jojoldu.tistory.com/297">참고</a>
 */
@Slf4j
@Configuration
@Profile({"test", "local"})
public class EmbeddedRedisConfig {
    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @PostConstruct
    public void runRedis() throws IOException {
        redisServer = new RedisServer(redisPort);
        redisServer.start();
        log.info("redisServer start..={}", redisPort);
    }

    @PreDestroy
    public void stopRedis() {
        if(!ObjectUtils.isEmpty(redisServer)) {
            redisServer.stop();
            log.info("redisServer stop..");
        }
    }

}
