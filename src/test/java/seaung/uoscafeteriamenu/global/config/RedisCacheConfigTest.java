package seaung.uoscafeteriamenu.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisKeyValueAdapter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisCacheConfigTest {

    @Autowired
    RedisCacheConfig redisCacheConfig;

    @Test
    @DisplayName("")
    void test() {
        // given
        //RedisKeyValueAdapter redisKeyValueAdapter = redisCacheConfig.redisKeyValueAdapter();
        //System.out.println(redisKeyValueAdapter.getClass());
        // when

        // then
    }
}