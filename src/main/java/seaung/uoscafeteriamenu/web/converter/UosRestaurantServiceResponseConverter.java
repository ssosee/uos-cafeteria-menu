package seaung.uoscafeteriamenu.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.cache.CacheSkillBlockService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.QuickReply;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UosRestaurantServiceResponseConverter {

    private final CacheSkillBlockService cacheSkillBlockService;

    /**
     * UosRestaurantMenuResponse을 SkillResponse로 변환
     */
    public SkillResponse toSkillResponseUseTextCardWithButtonAndQuickReplies(String version, UosRestaurantMenuResponse response) {

        // 연관된 QuickReply 블록 조회
        String parentBlockName = UosRestaurantName.fromKrName(response.getRestaurantName()).name();
        List<SkillBlock> skillBlocks = cacheSkillBlockService.getSkillBlocksByParentBlockName(parentBlockName)
                .stream()
                .map(SkillBlock::of)
                .collect(Collectors.toList());

        // 조식, 중식, 석식 quickReply 생성
        List<QuickReply> quickReplies = QuickReply.ofList(skillBlocks);

        // 추천 블록 조회
        SkillBlock recommendBlock = SkillBlock.of(cacheSkillBlockService.getSkillBlockByBlockName(BlockName.MENU_RECOMMEND));

        // output 생성
        Outputs outputs = Outputs.createOutputsUseTextCardWithRecommendButton(response, recommendBlock.getBlockId());

        // template 생성
        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));
        template.setQuickReplies(quickReplies);

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    /**
     * String을 SkillResponse로 변환
     */
    public SkillResponse toSkillResponseUseSimpleText(String version, String text) {
        return SkillResponse.ofSimpleText(version, text);
    }

    /**
     * List&lt;UosRestaurantMenuResponse&gt;을 SkillResponse로 변환
     */
    public SkillResponse toSkillResponseUseTextCardWithButton(String version, List<UosRestaurantMenuResponse> responses) {

        // 추천 블록 조회
        SkillBlock recommendBlock = SkillBlock.of(cacheSkillBlockService.getSkillBlockByBlockName(BlockName.MENU_RECOMMEND));

        // List<UosRestaurantMenuResponse>를 1개의 UosRestaurantMenuResponse로 매핑
        UosRestaurantMenuResponse response = findFirstMapUosRestaurantMenuResponse(responses);

        // output 생성
        Outputs outputs = Outputs.createOutputsUseTextCardWithRecommendButton(response, recommendBlock.getBlockId());

        // template 생성
        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    /**
     * List&lt;UosRestaurantMenuResponse&gt;을 SkillResponse로 변환
     */
    public SkillResponse toSkillResponseUseSimpleText(String version, List<UosRestaurantMenuResponse> responses) {

        // 발화 합치기
        String texts = joinMenuTexts(responses);

        // output 생성
        Outputs outputs = Outputs.createOutputsUseSimpleText(texts);

        // template 생성
        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    private UosRestaurantMenuResponse findFirstMapUosRestaurantMenuResponse(List<UosRestaurantMenuResponse> responses) {
        return responses.stream()
                .map(response -> UosRestaurantMenuResponse.builder()
                        .restaurantName(response.getRestaurantName())
                        .mealType(response.getMealType())
                        .menu(response.getMenu())
                        .view(response.getView())
                        .likeCount(response.getLikeCount())
                        .build())
                .findFirst().orElseThrow();
    }

    private String joinMenuTexts(List<UosRestaurantMenuResponse> responses) {
        return responses.stream()
                .map(UosRestaurantMenuResponse::getText)
                .collect(Collectors.joining("\n\n\n"));
    }
}
