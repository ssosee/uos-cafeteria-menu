package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import seaung.uoscafeteriamenu.domain.entity.Member;

import java.io.Serializable;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityManager.DEFAULT_TTL;

/**
 * Serializable 인터페이스를 구현하면 JVM에서 해당 객체는 저장하거나 다른 서버로 전송할 수 있도록 해준다.
 */
@RedisHash(timeToLive = DEFAULT_TTL)
@Getter
@NoArgsConstructor
public class CacheMember implements Serializable {

    @Id
    private String botUserId;
    private Long visitCount;
    @TimeToLive
    private int expiration;

    public static CacheMember create(String botUserId, Long visitCount, int expiration) {
        CacheMember cacheMember = new CacheMember();
        cacheMember.botUserId = botUserId;
        cacheMember.visitCount = visitCount;
        cacheMember.expiration = expiration;

        return cacheMember;
    }

    public static CacheMember of(Member member) {
        CacheMember cacheMember = new CacheMember();
        cacheMember.botUserId = member.getBotUserId();
        cacheMember.visitCount = member.getVisitCount();
        cacheMember.expiration = DEFAULT_TTL;

        return cacheMember;
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}
