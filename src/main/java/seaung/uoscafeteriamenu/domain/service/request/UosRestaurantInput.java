package seaung.uoscafeteriamenu.domain.service.request;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

@Data
public class UosRestaurantInput {
    private UosRestaurantName restaurantName;
    private MealType mealType;

    @Builder
    private UosRestaurantInput(UosRestaurantName restaurantName, MealType mealType) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
    }
}
