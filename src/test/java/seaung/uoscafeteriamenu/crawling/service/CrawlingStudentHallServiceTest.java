package seaung.uoscafeteriamenu.crawling.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingMealType;
import seaung.uoscafeteriamenu.crawling.crawler.StudentHallCrawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.StudentHall;
import seaung.uoscafeteriamenu.domain.repository.StudentHallRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CrawlingStudentHallServiceTest {

    @Autowired
    CrawlingStudentHallService crawlingStudentHallService;
    @Autowired
    StudentHallCrawler studentHallCrawler;
    @Autowired
    StudentHallRepository studentHallRepository;

    @Test
    @DisplayName("학생회관 8층 크롤링 데이터를 저장한다.")
    void saveAllCrawlingData() throws IOException {
        // given
        String restaurantName = UosRestaurantName.STUDENT_HALL.getKrName();
        String cssQuery = "div.listType02#week table tbody tr";
        String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

        // 의존하는게 좋을까...?
        List<UosRestaurantCrawlingResponse> responses = studentHallCrawler.crawlingFrom(restaurantName, studentHallUrl, cssQuery);

        // when
        crawlingStudentHallService.saveAllCrawlingData(responses);

        // then
        List<StudentHall> all = studentHallRepository.findAll();
        // 주 5일 3끼 제공 -> 15개의 식단
        assertAll(
                () -> assertThat(all).isNotEmpty(),
                () -> assertThat(all).hasSize(15)
                        .extracting("crawlingDate", "mealType", "menuDesc")
                        .contains(
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST, responses.get(0).getMenu().get(CrawlingMealType.BREAKFAST)),
                                tuple(responses.get(1).getRestaurantDate(), MealType.LUNCH, responses.get(1).getMenu().get(CrawlingMealType.LUNCH)),
                                tuple(responses.get(2).getRestaurantDate(), MealType.DINNER, responses.get(2).getMenu().get(CrawlingMealType.DINNER))
                        )
        );
    }
}