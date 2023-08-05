package seaung.uoscafeteriamenu.crawling.crawler;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StudentHallCrawler implements Crawler {

    private final Connection conn;

    public List<CrawlingResponse> crawlingFrom(String crawlingUrl, String cssQuery) throws IOException {

        Map<String, String> menu = new HashMap<>();
        List<CrawlingResponse> responses = new ArrayList<>();

        Elements elements = getElements(crawlingUrl, cssQuery);

        for(Element e : elements) {
            Elements dateAndMealType = e.select("th");
            Elements mealDesc = e.select("td");

            String date = dateAndMealType.get(0).text();
            CrawlingResponse response = new CrawlingResponse();
            response.setDate(date);

            for(Element mealType : dateAndMealType) {
                addMenu(mealType, CrawlerKey.BREAKFAST, mealDesc, menu);
                addMenu(mealType, CrawlerKey.LUNCH, mealDesc, menu);
                addMenu(mealType, CrawlerKey.DINNER, mealDesc, menu);
            }
            response.setMenu(menu);
            responses.add(response);
        }

        return responses;
    }

    private void addMenu(Element mealType, CrawlerKey key, Elements mealDesc, Map<String, String> menu) {
        if (mealType.text().equals(key.getKrName())) {
            StringBuilder sb = new StringBuilder();
            for (Node t : mealDesc.get(1).childNodes()) {
                if (t.toString().equals("<br>")) sb.append("\n");
                else sb.append(t);
            }
            String result = Parser.unescapeEntities(sb.toString(), false);
            menu.put(key.getKrName(), result);
        }
    }

    private Elements getElements(String crawlingUrl, String cssQuery) throws IOException {
        conn.url(crawlingUrl);
        Document document = conn.get();
        Elements elements = document.select(cssQuery);

        return elements;
    }
}
