package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;

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

        Outputs outputs = Outputs.createOutputsUseSimpleText(texts);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    public SkillResponse toSkillResponseUseTextCard(String version, String blockId) {
        String texts = joinMenuTexts();

        List<UosRestaurantInput> uosRestaurantInputs = getUosRestaurantInputs();

        Outputs outputs = Outputs.createOutputsUseTextCard(texts, blockId, uosRestaurantInputs.get(0));

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    private List<UosRestaurantInput> getUosRestaurantInputs() {
        return uosRestaurantsMenu.stream()
                .map(menu -> UosRestaurantInput.builder()
                        .restaurantName(UosRestaurantName.fromKrName(menu.getRestaurantName()))
                        .mealType(MealType.fromKrName(menu.getMealType()))
                        .build())
                .collect(Collectors.toList());
    }

    private String joinMenuTexts() {
        return this.uosRestaurantsMenu.stream()
                .map(UosRestaurantMenuResponse::getText)
                .collect(Collectors.joining("\n\n\n"));
    }
}
