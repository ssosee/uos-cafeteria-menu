package seaung.uoscafeteriamenu.crawling.crawler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class StudentHallCrawlerTest {

    @Autowired
    StudentHallCrawler studentHallCrawler;

    @Test
    @DisplayName("서울시립대학교 학생회관 1층 식당 주간식단표를 크롤링 한다.")
    void studentHallCrawler() throws IOException {
        // given
        String restaurantName = UosRestaurantName.STUDENT_HALL.getKrName();
        String cssQuery = "div.listType02#week table tbody tr";
        String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

        // when
        List<UosRestaurantCrawlingResponse> responses = studentHallCrawler.crawlingFrom(restaurantName, studentHallUrl, cssQuery);

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