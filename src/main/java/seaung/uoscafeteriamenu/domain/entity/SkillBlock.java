package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkillBlock extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String blockId;
    @Enumerated(EnumType.STRING)
    private BlockName blockName;
    private String action;
    private String label; // 사용자에게 노출될 문구
    private String messageText; // 사용자 측으로 노출될 발화
    private String parentBlockName; // 연관된 블록명

    @Builder
    private SkillBlock(String blockId, BlockName blockName, String action, String label, String messageText, String parentBlockName) {
        this.blockId = blockId;
        this.blockName = blockName;
        this.action = action;
        this.label = label;
        this.messageText = messageText;
        this.parentBlockName = parentBlockName;
    }

    public static SkillBlock create(String blockId, BlockName blockName, String action, String label, String messageText, String parentBlockName) {
        SkillBlock skillBlock = new SkillBlock();
        skillBlock.blockId = blockId;
        skillBlock.blockName = blockName;
        skillBlock.action = action;
        skillBlock.label = label;
        skillBlock.messageText = messageText;
        skillBlock.parentBlockName = parentBlockName;

        return skillBlock;
    }
}
