package seaung.uoscafeteriamenu;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CrawlingTest {

    private final String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000";
    private Connection conn;

    @Test
    @DisplayName("학생회관1층 식단표 크롤링")
    void test() throws IOException {
        // given
        conn =  Jsoup.connect(studentHallUrl);
        Document document = conn.get();
        Elements select = document.select("div.listType02#week table tbody tr");

        // when
        for (Element s : select) {
            Elements select1 = s.select("th");
            Elements select2 = s.select("td");

            System.out.println("날짜="+select1.get(0).text());
            for(Element s1 : select1) {
                if(s1.text().equals("조식")) {
                    System.out.println("조식 메뉴="+select2.get(0).ownText());
                } else if(s1.text().equals("중식")) {
                    System.out.println("중식 메뉴=");
                    StringBuilder sb = new StringBuilder();
                    for(Node t : select2.get(1).childNodes()) {
                        if(t.toString().equals("<br>")) sb.append("\n");
                        else sb.append(t);
                    }
                    String result = Parser.unescapeEntities(sb.toString(), false);
                    System.out.println(result);
                } else if(s1.text().equals("석식")) {
                    System.out.println("석식 메뉴=");
                    StringBuilder sb = new StringBuilder();
                    for(Node t : select2.get(1).childNodes()) {
                        if(t.toString().equals("<br>")) sb.append("\n");
                        else sb.append(t);
                    }
                    String result = Parser.unescapeEntities(sb.toString(), false);
                    System.out.println(result);
                }
            }
        }

        // then
    }
}
