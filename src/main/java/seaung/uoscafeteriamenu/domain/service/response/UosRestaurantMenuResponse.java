package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.OutputsDto;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
public class UosRestaurantMenuResponse {
    private Long id;
    private String restaurantName;
    private String mealType;
    private String menu;
    private Integer view;
    private Integer likeCount;

    @Builder
    private UosRestaurantMenuResponse(Long id, String restaurantName, String mealType, String menu, Integer view, Integer likeCount) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
        this.view = view;
        this.likeCount = likeCount;
    }

    public static UosRestaurantMenuResponse of(UosRestaurant uosRestaurant) {
        return UosRestaurantMenuResponse.builder()
                .id(uosRestaurant.getId())
                .restaurantName(uosRestaurant.getRestaurantName().getKrName())
                .menu(uosRestaurant.getMenuDesc())
                .mealType(uosRestaurant.getMealType().getKrName())
                .view(uosRestaurant.getView())
                .likeCount(uosRestaurant.getLikeCount())
                .build();
    }

    public static UosRestaurantMenuResponse of(CacheUosRestaurant cacheUosRestaurant) {
        return UosRestaurantMenuResponse.builder()
                .id(Long.valueOf(cacheUosRestaurant.getId()))
                .restaurantName(cacheUosRestaurant.getRestaurantName().getKrName())
                .menu(cacheUosRestaurant.getMenuDesc())
                .mealType(cacheUosRestaurant.getMealType().getKrName())
                .view(cacheUosRestaurant.getView())
                .likeCount(cacheUosRestaurant.getLikeCount())
                .build();
    }

    public static List<UosRestaurantMenuResponse> ofListByUosRestaurant(List<UosRestaurant> uosRestaurant) {
        return uosRestaurant.stream()
                .map(UosRestaurantMenuResponse::of)
                .collect(Collectors.toList());
    }

    public static List<UosRestaurantMenuResponse> ofListByCacheUosRestaurant(List<CacheUosRestaurant> cacheUosRestaurants) {
        return cacheUosRestaurants.stream()
                .map(UosRestaurantMenuResponse::of)
                .collect(Collectors.toList());
    }

    public static Page<UosRestaurantMenuResponse> ofPageByUosRestaurant(Page<UosRestaurant> uosRestaurants) {
        return uosRestaurants.map(UosRestaurantMenuResponse::of);
    }

    public static Page<UosRestaurantMenuResponse> ofPageByCacheUosRestaurant(Page<CacheUosRestaurant> cacheUosRestaurants) {
        return cacheUosRestaurants.map(UosRestaurantMenuResponse::of);
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
