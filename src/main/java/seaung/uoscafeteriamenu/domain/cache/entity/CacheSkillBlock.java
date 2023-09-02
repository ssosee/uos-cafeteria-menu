package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import seaung.uoscafeteriamenu.domain.entity.BaseTimeEntity;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;

import javax.persistence.*;

import java.io.Serializable;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityManager.DEFAULT_TTL;

@RedisHash(value = "cacheSkillBlock", timeToLive = DEFAULT_TTL)
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CacheSkillBlock implements Serializable {
    @Id
    private String blockId;
    private BlockName blockName;
    private String action;
    private String label; // 사용자에게 노출될 문구
    private String messageText; // 사용자 측으로 노출될 발화
    private String parentBlockName; // 연관된 블록명

    @Builder
    private CacheSkillBlock(String blockId, BlockName blockName, String action, String label, String messageText, String parentBlockName) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.action = action;
        this.label = label;
        this.messageText = messageText;
        this.parentBlockName = parentBlockName;
    }

    public static CacheSkillBlock of(SkillBlock skillBlock) {
        return CacheSkillBlock.builder()
                .blockId(skillBlock.getBlockId())
                .blockName(skillBlock.getBlockName())
                .action(skillBlock.getAction())
                .label(skillBlock.getLabel())
                .messageText(skillBlock.getMessageText())
                .parentBlockName(skillBlock.getParentBlockName())
                .build();
    }
}

