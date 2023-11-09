package seaung.uoscafeteriamenu.domain.cache.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
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
@Transactional
class CacheUosRestaurantRepositoryTest {

    @Autowired
    CacheUosRestaurantRepository cacheUosRestaurantRepository;
    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("캐시에서 리스트형태의 학식메뉴들을 조회한다.")
    void findByDateAndMealType() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "김밥", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2)); // id 때문에 생성

        CacheUosRestaurant cacheUosRestaurant1 = CacheUosRestaurant.of(uosRestaurant1);
        CacheUosRestaurant cacheUosRestaurant2 = CacheUosRestaurant.of(uosRestaurant2);
        cacheUosRestaurantRepository.saveAll(List.of(cacheUosRestaurant1, cacheUosRestaurant2));

        // when
        List<CacheUosRestaurant> cacheUosRestaurants = cacheUosRestaurantRepository.findByDateAndMealType(date, MealType.BREAKFAST);

        // then
        assertThat(cacheUosRestaurants).hasSize(2)
                .extracting("date", "restaurantName", "mealType", "menuDesc")
                .contains(
                        tuple(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면"),
                        tuple(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "김밥")
                );
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType,
                                              String menu, Integer view, Integer likeCount) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .view(view)
                .likeCount(likeCount)
                .build();
    }
}