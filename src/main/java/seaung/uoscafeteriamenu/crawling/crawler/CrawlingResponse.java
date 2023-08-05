package seaung.uoscafeteriamenu.crawling.crawler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
public class CrawlingResponse {
    private String date;
    private Map<String, String> menu;
}
