package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("cacheBotApikey")
@Getter
@NoArgsConstructor
public class CacheBotApikey implements Serializable {
    @Id
    private String botApikey;

    public CacheBotApikey(String botApikey) {
        this.botApikey = botApikey;
    }
}
