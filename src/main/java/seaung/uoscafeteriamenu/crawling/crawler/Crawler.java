package seaung.uoscafeteriamenu.crawling.crawler;

import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Crawler {

    // 크롤링 위치 때문에 생성
    protected final Map<CrawlingMealType, Integer> crawlingMealTypeIntegerMap = new HashMap<>();

    public Crawler() {
        // 조식, 중식, 석식
        crawlingMealTypeIntegerMap.put(CrawlingMealType.BREAKFAST, 0);
        crawlingMealTypeIntegerMap.put(CrawlingMealType.LUNCH, 1);
        crawlingMealTypeIntegerMap.put(CrawlingMealType.DINNER, 2);
    }

    public abstract List<UosRestaurantCrawlingResponse> crawlingFrom(String restaurantName, String url, String cssQuery) throws IOException;
}
