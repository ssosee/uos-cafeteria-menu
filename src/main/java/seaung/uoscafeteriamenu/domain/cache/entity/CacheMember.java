package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import seaung.uoscafeteriamenu.domain.entity.Member;

import java.io.Serializable;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityConfig.DEFAULT_TTL;

/**
 * Serializable 인터페이스를 구현하면 JVM에서 해당 객체는 저장하거나 다른 서버로 전송할 수 있도록 해준다.
 */
@RedisHash(value = "cacheMember", timeToLive = DEFAULT_TTL)
@Getter
@NoArgsConstructor
@ToString
public class CacheMember implements Serializable {

    @Id
    private String botUserId;
    private Long memberId;
    private Long visitCount;
    @TimeToLive
    private int expiration;

    @Builder
    private CacheMember(String botUserId, Long memberId, Long visitCount, int expiration) {
        this.botUserId = botUserId;
        this.memberId = memberId;
        this.visitCount = visitCount;
        this.expiration = expiration;
    }

    public static CacheMember create(String botUserId, Long memberId, Long visitCount, int expiration) {
        return CacheMember.builder()
                .botUserId(botUserId)
                .memberId(memberId)
                .visitCount(visitCount)
                .expiration(expiration)
                .build();
    }

    public static CacheMember of(Member member) {
        return CacheMember.builder()
                .botUserId(member.getBotUserId())
                .memberId(member.getId())
                .visitCount(member.getVisitCount())
                .expiration(DEFAULT_TTL)
                .build();
    }

    public void increaseVisitCount() {
        this.visitCount++;
    }
}
