package seaung.uoscafeteriamenu.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.QuickReply;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.OutputsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UosRestaurantServiceResponseConverter {

    private final UosRestaurantService uosRestaurantService;
    private final SkillBlockRepository skillBlockRepository;

    public SkillResponse getUosRestaurantMenuToSkillResponseUseTextCardWithButtonAndQuickReplies(String version, UosRestaurantInput input) {

        // 식당이름, 식사종류로 금일 식당 메뉴 조회
        UosRestaurantMenuResponse response = uosRestaurantService.getUosRestaurantMenu(input);

        // 챗봇의 발화 생성
        String text = createText(response);

        // 연관된 QuickReply 블록 조회
        List<SkillBlock> skillBlocks = skillBlockRepository.findByParentBlockNameContains(input.getRestaurantName().name());

        // 조식, 중식, 석식 quickReply 생성
        List<QuickReply> quickReplies = QuickReply.ofList(skillBlocks);

        // 추천 블록 조회
        SkillBlock recommendBlock = skillBlockRepository.findByBlockName(BlockName.MENU_RECOMMEND)
                .orElseThrow(RuntimeException::new);

        // OutputsDto 생성
        OutputsDto outputsDto = OutputsDto.createOutputDto(text, recommendBlock.getBlockId(), input);

        // output 조회
        Outputs outputs = Outputs.findOutputs(outputsDto);

        // template 생성
        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));
        template.setQuickReplies(quickReplies);

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    private String createText(UosRestaurantMenuResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(response.getRestaurantName());
        sb.append("(").append(response.getMealType()).append(")\n");
        sb.append("👀 조회수: ").append(response.getView()).append("\n");
        sb.append("👍 추천수: ").append(response.getLikeCount()).append("\n\n");
        sb.append(response.getMenu());

        return sb.toString();
    }
}
