package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.RecommendUosRestaurantMenuInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
public class SkillPayload {
    private Intent intent; // 발화와 일치하는 블록의 정보
    private UserRequest userRequest;
    private Bot bot;
    private Action action;


    @Builder
    private SkillPayload(Intent intent, UserRequest userRequest, Bot bot, Action action) {
        this.intent = intent;
        this.userRequest = userRequest;
        this.bot = bot;
        this.action = action;
    }

    public UosRestaurantInput toUosRestaurantInput(TimeProvider timeProvider) {
        return UosRestaurantInput.builder()
                .botUserId(userRequest.getUser().getId())
                .date(CrawlingUtils.toDateString(timeProvider.getCurrentLocalDateTime()))
                .restaurantName(UosRestaurantName.fromName(action.getParams().get("restaurantName")))
                .mealType(MealType.valueOf(action.getParams().get("mealType")))
                .build();
    }

    public RecommendUosRestaurantMenuInput toUosRestaurantInputUseActionClientExtra(TimeProvider timeProvider) {
        return RecommendUosRestaurantMenuInput.builder()
                .botUserId(userRequest.getUser().getId())
                .date(CrawlingUtils.toDateString(timeProvider.getCurrentLocalDateTime()))
                .restaurantName(UosRestaurantName.fromName(action.getClientExtra().get("restaurantName")))
                .mealType(MealType.valueOf(action.getClientExtra().get("mealType")))
                .build();
    }

    public UosRestaurantsInput toUosRestaurantsInput(TimeProvider timeProvider) {
        return UosRestaurantsInput.builder()
                .botUserId(userRequest.getUser().getId())
                .date(CrawlingUtils.toDateString(timeProvider.getCurrentLocalDateTime()))
                .mealType(MealType.valueOf(action.getParams().get("mealType")))
                .build();
    }
}
