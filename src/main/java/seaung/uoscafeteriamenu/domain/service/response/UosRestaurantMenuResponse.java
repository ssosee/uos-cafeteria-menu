package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.OutputsDto;

import java.util.List;
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

    public static UosRestaurantMenuResponse of(UosRestaurant uosRestaurant) {
        return UosRestaurantMenuResponse.builder()
                .restaurantName(uosRestaurant.getRestaurantName().getKrName())
                .menu(uosRestaurant.getMenuDesc())
                .mealType(uosRestaurant.getMealType().getKrName())
                .view(uosRestaurant.getView())
                .likeCount(uosRestaurant.getLikeCount())
                .build();
    }

    public static List<UosRestaurantMenuResponse> ofList(List<UosRestaurant> uosRestaurant) {
        return uosRestaurant.stream()
                .map(UosRestaurantMenuResponse::of)
                .collect(Collectors.toList());
    }

    public static Page<UosRestaurantMenuResponse> ofPage(Page<UosRestaurant> uosRestaurant) {
        return uosRestaurant.map(UosRestaurantMenuResponse::of);
    }

    public SkillResponse toSkillResponseUseSimpleText(String version) {

        String text = getText();

        OutputsDto outputsDto = OutputsDto.builder()
                .text(text)
                .build();

        Outputs outputs = Outputs.createOutputs(outputsDto);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(restaurantName);
        sb.append("(").append(mealType).append(")\n");
        sb.append("👀 조회수: ").append(view).append("\n");
        sb.append("👍 추천수: ").append(likeCount).append("\n\n");
        sb.append(menu);

        return sb.toString();
    }
}
