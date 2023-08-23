package seaung.uoscafeteriamenu.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.repository.memory.SkillBlockMemoryRepository;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.QuickReply;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.OutputsDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UosRestaurantServiceResponseConverter {

    private final UosRestaurantService uosRestaurantService;
    private final SkillBlockMemoryRepository skillBlockMemoryRepository;

    public SkillResponse getUosRestaurantMenuToSkillResponse(String version, String blockId, UosRestaurantInput input) {
        UosRestaurantMenuResponse response = uosRestaurantService.getUosRestaurantMenu(input);

        String text = getText(response);

        OutputsDto outputsDto = OutputsDto.builder()
                .text(text)
                .blockId(blockId)
                .input(input)
                .build();

        Outputs outputs = Outputs.findOutputs(outputsDto);

        List<BlockName> blockNames = skillBlockMemoryRepository.findAllBlockNameByUosRestaurantName(input.getRestaurantName());
        //List<QuickReply> quickReplies = createQuickRepliesUosRestaurantBlock();

        return null;
    }

//    public SkillResponse toSkillResponseUseTextCard(String version, String blockId, UosRestaurantInput input) {
//
//        String text = getText();
//
//        OutputsDto outputsDto = OutputsDto.builder()
//                .text(text)
//                .blockId(blockId)
//                .input(input)
//                .build();
//
//        Outputs outputs = Outputs.findOutputs(outputsDto);
//
//        // 조식, 중식, 석식 quickReply 생성
//        //List<QuickReply> quickReplies = QuickReply.createQuickRepliesUosRestaurantBlock();
//
//        SkillTemplate template = new SkillTemplate();
//        template.setOutputs(List.of(outputs));
//        //template.setQuickReplies(quickReplies);
//
//        return SkillResponse.builder()
//                .version(version)
//                .template(template)
//                .build();
//    }

    private String getText(UosRestaurantMenuResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(response.getRestaurantName());
        sb.append("(").append(response.getMealType()).append(")\n");
        sb.append("👀 조회수: ").append(response.getView()).append("\n");
        sb.append("👍 추천수: ").append(response.getLikeCount()).append("\n\n");
        sb.append(response.getMenu());

        return sb.toString();
    }
}
