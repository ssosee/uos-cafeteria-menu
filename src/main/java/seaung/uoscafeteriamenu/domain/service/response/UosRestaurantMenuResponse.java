package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;

import java.util.ArrayList;
import java.util.List;

@Data
public class UosRestaurantMenuResponse<T> {
    private String restaurantName;
    private String mealType;
    private String menu;

    @Builder
    private UosRestaurantMenuResponse(String restaurantName, String mealType, String menu) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
    }

    public SkillResponse<T> toSkillResponse(String version) {

        SkillTemplate<T> template = new SkillTemplate<>();
        template.setOutputs(new ArrayList<T>());

        return SkillResponse.<T>builder()
                .version(version)
                .template(template)
                .build();
    }
}
