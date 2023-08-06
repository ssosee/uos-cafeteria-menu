package seaung.uoscafeteriamenu.web.controller.request;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;

@Data
public class UosRestaurantRequest {
    private String restaurantName;
    private String mealType;

    @Builder
    private UosRestaurantRequest(String restaurantName, String mealType) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
    }

    public UosRestaurantInput toServiceInput() {
        return UosRestaurantInput.builder()
                .mealType(MealType.valueOf(mealType))
                .restaurantName(UosRestaurantName.valueOf(restaurantName))
                .build();
    }
}
