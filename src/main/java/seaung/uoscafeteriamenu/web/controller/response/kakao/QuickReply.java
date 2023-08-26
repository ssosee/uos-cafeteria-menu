package seaung.uoscafeteriamenu.web.controller.response.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.memory.SkillBlockMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuickReply {
    private String label;
    private String action;
    private String messageText;
    private String blockId;
    private Map<String, String> extra;

    @Builder
    private QuickReply(String label, String action, String messageText, String blockId, Map<String, String> extra) {
        this.label = label;
        this.action = action;
        this.messageText = messageText;
        this.blockId = blockId;
        this.extra = extra;
    }

    public static QuickReply of(SkillBlock skillBlock) {
        return QuickReply.builder()
                .blockId(skillBlock.getBlockId())
                .label(skillBlock.getLabel())
                .action(skillBlock.getAction())
                .messageText(skillBlock.getMessageText())
                .build();
    }

    public static List<QuickReply> ofList(List<SkillBlock> skillBlocks) {
        return skillBlocks.stream()
                .map(QuickReply::of)
                .collect(Collectors.toList());
    }
}
