package seaung.uoscafeteriamenu.crawling.crawler;

import java.io.IOException;
import java.util.List;

public interface Crawler {
    List<CrawlingResponse> crawlingFrom(String url, String cssQuery) throws IOException;
}
