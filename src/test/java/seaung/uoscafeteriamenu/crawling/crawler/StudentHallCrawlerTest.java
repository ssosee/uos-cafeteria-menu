package seaung.uoscafeteriamenu.crawling.crawler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@SpringBootTest
class StudentHallCrawlerTest {

    @MockBean
    UosRestarantCrawler crawler;

    @Test
    @DisplayName("서울시립대학교 학생회관 1층 식당 주간식단표를 크롤링 한다.")
    void studentHallCrawler() throws IOException {
        // given
        String restaurantName = UosRestaurantName.STUDENT_HALL.getKrName();
        String cssQuery = "div.listType02#week table tbody tr";
        String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

        // when
        // 크롤링 타켓이 변할 수 있기 때문에 mocking
        UosRestaurantCrawlingResponse crawlingResponse = new UosRestaurantCrawlingResponse(UosRestaurantName.STUDENT_HALL.getKrName(), "8/15 (화)");
        crawlingResponse.setMenu(Map.of(CrawlingMealType.BREAKFAST, "A코스 떡라면, 김치, 단무지, 공기밥 -> 3000원",
                CrawlingMealType.LUNCH, "B코스 돈까스, 총각김치, 단무지, 쇠고기스프 -> 5500원",
                CrawlingMealType.DINNER, "C코스 김치제육, 갓김치, 단무지, 어묵조림 -> 5500원"));
        List<UosRestaurantCrawlingResponse> responses = List.of(crawlingResponse);
        when(crawler.crawlingFrom(restaurantName, studentHallUrl, cssQuery)).thenReturn(responses);

        // then
        assertThat(responses).isNotEmpty();

        responses.stream()
                .forEach(response -> {
                    assertThat(response.getRestaurantName()).isEqualTo(restaurantName);
                    assertThat(response.getRestaurantDate()).isNotBlank();
                    System.out.println(response.getRestaurantName());
                    System.out.println(response.getRestaurantDate());
                    response.getMenu().entrySet().forEach(System.out::println);
                });
    }
}