package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.TextCard;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class UosRestaurantMenuResponse {
    private String restaurantName;
    private String mealType;
    private String menu;
    private Integer view;
    private Integer likeCount;

    @Builder
    private UosRestaurantMenuResponse(String restaurantName, String mealType, String menu, Integer view, Integer likeCount) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
        this.view = view;
        this.likeCount = likeCount;
    }

    public SkillResponse toSkillResponseUseSimpleText(String version) {

        String text = getText();

        Outputs outputs = createOutputsUseSimpleText(text);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    public SkillResponse toSkillResponseUseTextCard(String version, String blockId, UosRestaurantInput input) {

        String text = getText();
        Outputs outputs = findOutputs(blockId, input, text);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    // 메뉴를 찾을 수 없거나 학교에서 제공한 메뉴가 없으면 simpleText를 반환한다.
    private Outputs findOutputs(String blockId, UosRestaurantInput input, String text) {
        if(text.contains(UosRestaurantMenuException.NOT_FOUND_MENU) || text.contains(CrawlingUtils.NOT_PROVIDED_MENU)) {
            return createOutputsUseSimpleText(text);
        }
        return createOutputsUseTextCard(text, blockId, input);
    }


    private Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }

    private Outputs createOutputsUseTextCard(String text, String blockId, UosRestaurantInput input) {

        Map<String, String> extra = new HashMap<>();
        extra.put("restaurantName", input.getRestaurantName().name());
        extra.put("mealType", input.getMealType().name());

        Button button = Button.builder()
                .label("추천 😋")
                .action("block")
                .blockId(blockId)
                .extra(extra)
                .build();

        return Outputs.builder()
                .textCard(new TextCard(text, List.of(button)))
                .build();
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(restaurantName);
        sb.append("(").append(mealType).append(")\n");
        sb.append("👀 조회수: ").append(view).append("\n");
        sb.append("👍 좋아요: ").append(likeCount).append("\n\n");
        sb.append(menu);

        return sb.toString();
    }
}
