package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UosRestaurantsMenuResponse {

    private List<UosRestaurantMenuResponse> uosRestaurantsMenu;

    public UosRestaurantsMenuResponse(List<UosRestaurantMenuResponse> uosRestaurantsMenu) {
        this.uosRestaurantsMenu = uosRestaurantsMenu;
    }

    public SkillResponse toSkillResponseUseSimpleText(String version) {

        String texts = joinMenuTexts();

        Outputs outputs = createOutputsUseSimpleText(texts);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    private String joinMenuTexts() {
        return this.uosRestaurantsMenu.stream()
                .map(UosRestaurantMenuResponse::getText)
                .collect(Collectors.joining("\n\n\n"));
    }

    private Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }
}
