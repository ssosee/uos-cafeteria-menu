package seaung.uoscafeteriamenu.domain.service.request;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

@Data
public class UosRestaurantsInput {
    private String date;
    private MealType mealType;
    private String botUserId;

    @Builder
    private UosRestaurantsInput(String date, MealType mealType, String botUserId) {
        this.date = date;
        this.mealType = mealType;
        this.botUserId = botUserId;
    }
}
