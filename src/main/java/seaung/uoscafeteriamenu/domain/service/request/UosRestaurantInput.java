package seaung.uoscafeteriamenu.domain.service.request;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

@Data
public class UosRestaurantInput {
    private String date;
    private UosRestaurantName restaurantName;
    private MealType mealType;
    private String botUserId;

    @Builder
    private UosRestaurantInput(String date, UosRestaurantName restaurantName, MealType mealType, String botUserId) {
        this.date = date;
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.botUserId = botUserId;
    }
}
