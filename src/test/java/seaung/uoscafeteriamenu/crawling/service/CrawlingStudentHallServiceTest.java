package seaung.uoscafeteriamenu.crawling.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingMealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestarantCrawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class CrawlingStudentHallServiceTest {

    @Autowired
    CrawlingUosRestaurantService crawlingStudentHallService;
    @MockBean
    UosRestarantCrawler studentHallCrawler;
    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    String restaurantName = UosRestaurantName.STUDENT_HALL.getKrName();
    String cssQuery = "div.listType02#week table tbody tr";
    String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";

    @Test
    @DisplayName("크롤링 데이터를 저장한다.")
    void saveAllCrawlingData() {
        // given
        // 크롤링 대상은 변할 수 있기 때문에 크롤링 구현체를 사용하지 않는다.
        UosRestaurantCrawlingResponse crawlingResponse = new UosRestaurantCrawlingResponse(UosRestaurantName.STUDENT_HALL.getKrName(), "8/15 (화)");
        crawlingResponse.setMenu(Map.of(CrawlingMealType.BREAKFAST, "A코스 떡라면, 김치, 단무지, 공기밥 -> 3000원",
                CrawlingMealType.LUNCH, "B코스 돈까스, 총각김치, 단무지, 쇠고기스프 -> 5500원",
                CrawlingMealType.DINNER, "C코스 김치제육, 갓김치, 단무지, 어묵조림 -> 5500원"));
        List<UosRestaurantCrawlingResponse> responses = List.of(crawlingResponse);

        // when
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));

        // then
        List<UosRestaurant> all = uosRestaurantRepository.findAll();

        assertAll(
                () -> assertThat(all).isNotEmpty(),
                () -> assertThat(all).hasSize(3)
                        .extracting("crawlingDate", "mealType", "menuDesc")
                        .contains(
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST, "A코스 떡라면, 김치, 단무지, 공기밥 -> 3000원"),
                                tuple(responses.get(0).getRestaurantDate(), MealType.LUNCH, "B코스 돈까스, 총각김치, 단무지, 쇠고기스프 -> 5500원"),
                                tuple(responses.get(0).getRestaurantDate(), MealType.DINNER, "C코스 김치제육, 갓김치, 단무지, 어묵조림 -> 5500원")
                        )
        );
    }

    @Test
    @DisplayName("이미 크롤링한 데이터가 있으면 데이터베이스에 저장하지 않는다.")
    void saveNotDuplicateCrawlingData() throws IOException {
        // given
        // 크롤링 대상은 변할 수 있기 때문에 크롤링 구현체를 사용하지 않는다.
        UosRestaurantCrawlingResponse crawlingResponse = new UosRestaurantCrawlingResponse(UosRestaurantName.STUDENT_HALL.getKrName(), "8/15 (화)");
        crawlingResponse.setMenu(Map.of(CrawlingMealType.BREAKFAST, "A코스 떡라면, 김치, 단무지, 공기밥 -> 3000원",
                CrawlingMealType.LUNCH, "B코스 돈까스, 총각김치, 단무지, 쇠고기스프 -> 5500원",
                CrawlingMealType.DINNER, "C코스 김치제육, 갓김치, 단무지, 어묵조림 -> 5500원"));
        List<UosRestaurantCrawlingResponse> responses = List.of(crawlingResponse);

        // when
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));
        crawlingStudentHallService.saveAllCrawlingData(List.of(responses));

        // then
        List<UosRestaurant> all = uosRestaurantRepository.findAll();

        assertAll(
                () -> assertThat(all).isNotEmpty(),
                () -> assertThat(all).hasSize(3)
                        .extracting("crawlingDate", "mealType", "menuDesc")
                        .contains(
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST, "A코스 떡라면, 김치, 단무지, 공기밥 -> 3000원"),
                                tuple(responses.get(0).getRestaurantDate(), MealType.LUNCH, "B코스 돈까스, 총각김치, 단무지, 쇠고기스프 -> 5500원"),
                                tuple(responses.get(0).getRestaurantDate(), MealType.DINNER, "C코스 김치제육, 갓김치, 단무지, 어묵조림 -> 5500원")
                        )
        );
    }
}