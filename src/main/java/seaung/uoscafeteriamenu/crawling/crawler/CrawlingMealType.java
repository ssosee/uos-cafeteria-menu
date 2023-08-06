package seaung.uoscafeteriamenu.crawling.crawler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrawlingMealType {
    BREAKFAST("조식"),
    LUNCH("중식"),
    DINNER("석식");

    private final String krName;
}
