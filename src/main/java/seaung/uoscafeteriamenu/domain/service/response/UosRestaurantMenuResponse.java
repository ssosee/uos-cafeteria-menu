package seaung.uoscafeteriamenu.domain.service.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UosRestaurantMenuResponse {
    private String restaurantName;
    private String mealType;
    private String menu;

    @Builder
    private UosRestaurantMenuResponse(String restaurantName, String mealType, String menu) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
    }
}
