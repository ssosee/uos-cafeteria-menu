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
        String menuDesc = "11:30~14:00\n" +
                "(6,000원)\n" +
                "닭갈비볶음밥\n" +
                "Stir-fried Chicken Fried Rice\n" +
                "콩나물국\n" +
                "두반장가지볶음\n" +
                "실곤약무침\n" +
                "양배추찜\n" +
                "\n" +
                "닭정육 : 브라질산,\n" +
                "돈육 : 국내산\n" +
                "984kcal/38g";

        // 크롤링 대상은 변할 수 있기 때문에 크롤링 구현체를 사용하지 않는다.
        UosRestaurantCrawlingResponse crawlingResponse = new UosRestaurantCrawlingResponse(UosRestaurantName.STUDENT_HALL.getKrName(), "8/15 (화)");
        crawlingResponse.setMenu(Map.of(CrawlingMealType.BREAKFAST, "A코스"+menuDesc,
                CrawlingMealType.LUNCH, "B코스"+menuDesc,
                CrawlingMealType.DINNER, "C코스"+menuDesc));
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
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST, "A코스"+menuDesc),
                                tuple(responses.get(0).getRestaurantDate(), MealType.LUNCH, "B코스"+menuDesc),
                                tuple(responses.get(0).getRestaurantDate(), MealType.DINNER, "C코스"+menuDesc)
                        )
        );
    }

    @Test
    @DisplayName("이미 크롤링한 데이터가 있으면 데이터베이스에 저장하지 않는다.")
    void saveNotDuplicateCrawlingData() throws IOException {
        // given
        String menuDesc = "11:30~14:00\n" +
                "(6,000원)\n" +
                "닭갈비볶음밥\n" +
                "Stir-fried Chicken Fried Rice\n" +
                "콩나물국\n" +
                "두반장가지볶음\n" +
                "실곤약무침\n" +
                "양배추찜\n" +
                "\n" +
                "닭정육 : 브라질산,\n" +
                "돈육 : 국내산\n" +
                "984kcal/38g";
        // 크롤링 대상은 변할 수 있기 때문에 크롤링 구현체를 사용하지 않는다.
        UosRestaurantCrawlingResponse crawlingResponse = new UosRestaurantCrawlingResponse(UosRestaurantName.STUDENT_HALL.getKrName(), "8/15 (화)");
        crawlingResponse.setMenu(Map.of(CrawlingMealType.BREAKFAST, "A코스"+menuDesc,
                CrawlingMealType.LUNCH, "B코스"+menuDesc,
                CrawlingMealType.DINNER, "C코스"+menuDesc));
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
                                tuple(responses.get(0).getRestaurantDate(), MealType.BREAKFAST, "A코스"+menuDesc),
                                tuple(responses.get(0).getRestaurantDate(), MealType.LUNCH, "B코스"+menuDesc),
                                tuple(responses.get(0).getRestaurantDate(), MealType.DINNER, "C코스"+menuDesc)
                        )
        );
    }
}