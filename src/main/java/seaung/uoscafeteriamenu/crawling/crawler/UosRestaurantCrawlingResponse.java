package seaung.uoscafeteriamenu.crawling.crawler;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
public class UosRestaurantCrawlingResponse {
    private String restaurantName;
    private String restaurantDate;
    private Map<CrawlingMealType, String> menu;

    public UosRestaurantCrawlingResponse(String restaurantName, String restaurantDate) {
        this.restaurantName = restaurantName;
        this.restaurantDate = restaurantDate;
    }
}
