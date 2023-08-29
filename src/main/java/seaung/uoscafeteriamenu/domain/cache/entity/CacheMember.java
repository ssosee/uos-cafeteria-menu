package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import seaung.uoscafeteriamenu.domain.entity.Member;

@Getter
@RedisHash("cacheMember")
public class CacheMember {

    public static final Long DEFAULT_TTL = 3600L;

    @Id
    private String botUserId;
    private Long visitCount;
    @TimeToLive
    private Long expiration;

    public static CacheMember create(String botUserId, Long visitCount, Long expiration) {
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

    public void increaseViewCount() {
        this.visitCount++;
    }
}
