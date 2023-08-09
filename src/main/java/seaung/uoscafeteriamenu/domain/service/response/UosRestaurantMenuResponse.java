package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;

import java.util.ArrayList;
import java.util.List;

@Data
public class UosRestaurantMenuResponse {
    private String restaurantName;
    private String mealType;
    private String menu;

    @Builder
    private UosRestaurantMenuResponse(String restaurantName, String mealType, String menu) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
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

    private static Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }

    private String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(restaurantName);
        sb.append("(").append(mealType).append(")\n\n");
        sb.append(menu);

        return sb.toString();
    }
}
