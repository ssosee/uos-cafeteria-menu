package seaung.uoscafeteriamenu.crawling.crawler;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UosRestarantCrawler extends Crawler {

    private final Connection conn;
    // 크롤링 위치 때문에 생성

    @Autowired
    public UosRestarantCrawler(Connection conn) {
        this.conn = conn;
    }

    public List<UosRestaurantCrawlingResponse> crawlingFrom(String restaurantName, String crawlingUrl, String cssQuery) throws IOException {

        List<UosRestaurantCrawlingResponse> responses = new ArrayList<>();

        Elements elements = getElements(crawlingUrl, cssQuery);

        for(Element e : elements) {
            Elements dateAndMealType = e.select("th");
            Elements mealDesc = e.select("td");

            String date = dateAndMealType.get(0).text();
            UosRestaurantCrawlingResponse response = new UosRestaurantCrawlingResponse(restaurantName, date);
            Map<CrawlingMealType, String> menu = new HashMap<>();

            for(int i = 1; i < dateAndMealType.size(); i++) {
                addMenu(dateAndMealType.get(i), CrawlingMealType.BREAKFAST, mealDesc, menu);
                addMenu(dateAndMealType.get(i), CrawlingMealType.LUNCH, mealDesc, menu);
                addMenu(dateAndMealType.get(i), CrawlingMealType.DINNER, mealDesc, menu);
            }

            response.setMenu(menu);
            responses.add(response);
        }

        return responses;
    }


    private int convertCrawlingMealTypeToNumber(CrawlingMealType crawlingMealType) {
        return crawlingMealTypeIntegerMap.get(crawlingMealType);
    }

    private void addMenu(Element mealType, CrawlingMealType crawlingMealType, Elements mealDesc, Map<CrawlingMealType, String> menu) {
        if (mealType.text().equals(crawlingMealType.getKrName())) {
            int crawlingMealTypeToNumber = convertCrawlingMealTypeToNumber(crawlingMealType);
            StringBuilder sb = new StringBuilder();

            for (Node t : mealDesc.get(crawlingMealTypeToNumber).childNodes()) {
                if (t.toString().equals("<br>")) sb.append("\n");
                else sb.append(t);
            }

            String resultMenu = Parser.unescapeEntities(sb.toString(), false);
            menu.put(crawlingMealType, resultMenu);
        }
    }

    private Elements getElements(String crawlingUrl, String cssQuery) throws IOException {
        conn.url(crawlingUrl);
        Document document = conn.get();
        Elements elements = document.select(cssQuery);

        return elements;
    }
}
