package seaung.uoscafeteriamenu.crawling.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheUosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.*;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE;

@SpringBootTest
class CrawlingCacheUosRestaurantServiceTest {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;
    @Autowired
    CacheUosRestaurantRepository cacheUosRestaurantRepository;
    @Autowired
    CrawlingCacheUosRestaurantService crawlingCacheUosRestaurantService;

    @AfterEach
    void tearDown() {
        cacheUosRestaurantRepository.deleteAll();
    }

    @Test
    @DisplayName("학교 식단 정보를 캐시에 저장한다.")
    void saveAllCrawlingDataInRedis() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1);
        List<UosRestaurant> uosRestaurants = List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4);

        // when
        crawlingCacheUosRestaurantService.saveAllCrawlingDataInRedis(uosRestaurants);

        // then
        Iterable<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findAll();
        assertThat(cacheUosRestaurants).hasSize(4)
                .extracting("date", "restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .contains(
                        tuple(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0),
                        tuple(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0),
                        tuple(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0),
                        tuple(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1)
                );
    }

    private UosRestaurant createUosRestaurant(String crawlingDate, UosRestaurantName uosRestaurantName, MealType mealType, String menu, Integer view, Integer likeCount) {
        return UosRestaurant.builder()
                .crawlingDate(crawlingDate)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .view(view)
                .likeCount(likeCount)
                .build();
    }
}