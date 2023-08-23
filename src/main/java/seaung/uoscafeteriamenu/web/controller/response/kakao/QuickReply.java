package seaung.uoscafeteriamenu.web.controller.response.kakao;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.memory.SkillBlockMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
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

    public static List<QuickReply> createQuickRepliesUosRestaurantBlock(List<BlockName> blockNames) {
        List<QuickReply> quickReplies = new ArrayList<>();

//        for(BlockName name : blockNames) {
//            QuickReply quickReply = QuickReply.builder()
//                    .blockId()
//                    .build();
//        }

        return quickReplies;
    }
}
