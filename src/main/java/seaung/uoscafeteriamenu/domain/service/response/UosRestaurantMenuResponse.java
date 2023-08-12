package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.TextCard;

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

    @Builder
    private UosRestaurantMenuResponse(String restaurantName, String mealType, String menu, Integer view) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
        this.view = view;
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

    public SkillResponse toSkillResponseUseTextCard(String version) {
        String text = getText();

        Outputs outputs = createOutputsUseTextCard(text);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    private Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }

    private Outputs createOutputsUseTextCard(String text) {

        Map<String, String> extra = new HashMap<>();
        extra.put("restaurantName", "STUDENT_HALL");
        extra.put("mealType", "BREAKFAST");

        Button button = Button.builder()
                .label("좋아요")
                .action("block")
                //.blockId("64ad54f04bc96323949bfb33")
                .messageText("밍밍이~ 좋아요")
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
        sb.append("- 조회수:").append(view).append("\n\n");
        sb.append(menu);

        return sb.toString();
    }
}
