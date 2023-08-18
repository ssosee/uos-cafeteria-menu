package seaung.uoscafeteriamenu.crawling.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestarantCrawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CrawlingStudentHallServiceTest {

    @Autowired
    CrawlingUosRestaurantService crawlingStudentHallService;
    @Autowired
    UosRestarantCrawler studentHallCrawler;
    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

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
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));

        // then
        List<UosRestaurant> all = uosRestaurantRepository.findAll();

        // 주 5일 3끼 제공 -> 15개의 식단
        assertAll(
                () -> assertThat(all).isNotEmpty(),
                () -> assertThat(all).hasSize(15)
                        .extracting("crawlingDate", "mealType")
                        .contains(
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST),
                                tuple(responses.get(1).getRestaurantDate(), MealType.LUNCH),
                                tuple(responses.get(2).getRestaurantDate(), MealType.DINNER)
                        )
        );
    }

    @Test
    @DisplayName("이미 크롤링한 데이터가 있으면 데이터베이스에 저장하지 않는다.")
    void saveNotDuplicateCrawlingData() throws IOException {
        // given
        String restaurantName = UosRestaurantName.STUDENT_HALL.getKrName();
        String cssQuery = "div.listType02#week table tbody tr";
        String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

        // 의존하는게 좋을까...?
        List<UosRestaurantCrawlingResponse> responses = studentHallCrawler.crawlingFrom(restaurantName, studentHallUrl, cssQuery);

        // when
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));

        // then
        List<UosRestaurant> all = uosRestaurantRepository.findAll();

        // 주 5일 3끼 제공 -> 15개의 식단
        assertAll(
                () -> assertThat(all).isNotEmpty(),
                () -> assertThat(all).hasSize(15)
                        .extracting("crawlingDate", "mealType")
                        .contains(
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST),
                                tuple(responses.get(1).getRestaurantDate(), MealType.LUNCH),
                                tuple(responses.get(2).getRestaurantDate(), MealType.DINNER)
                        )
        );
    }
}