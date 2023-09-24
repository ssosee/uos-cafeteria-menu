package seaung.uoscafeteriamenu.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.*;

@DataJpaTest
class UosRestaurantRepositoryTest {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("식당이름, 식사종류를 조건으로 식당 메뉴를 조회한다.")
    void findByRestaurantNameAndMealType() {
        // given
        UosRestaurant uosRestaurant = createUosRestaurant(STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        // when
        UosRestaurant result = uosRestaurantRepository.findByRestaurantNameAndMealType(STUDENT_HALL, MealType.BREAKFAST).get();

        // then
        assertAll(
                () -> assertThat(result.getRestaurantName()).isEqualTo(STUDENT_HALL),
                () -> assertThat(result.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(result.getMenuDesc()).isEqualTo("라면")
        );
    }

    @Test
    @DisplayName("날짜, 식당이름, 식사종류를 조건으로 식당 메뉴를 조회한다.")
    void findByCrawlingDateAndRestaurantNameAndMealType() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        // when
        UosRestaurant result = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(date, STUDENT_HALL, MealType.BREAKFAST).get();

        // then
        assertAll(
                () -> assertThat(result.getCrawlingDate()).isEqualTo(date),
                () -> assertThat(result.getRestaurantName()).isEqualTo(STUDENT_HALL),
                () -> assertThat(result.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(result.getMenuDesc()).isEqualTo("라면")
        );
    }

    @Test
    @DisplayName("날짜, 식사종류를 조건으로 식당 메뉴들을 조회한다.")
    void findByCrawlingDateAndAndMealType() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면");
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥");
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스");
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육");
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        // when
        List<UosRestaurant> result = uosRestaurantRepository.findByCrawlingDateAndMealType(date, MealType.BREAKFAST);

        // then
        assertThat(result).hasSize(4)
                .extracting("crawlingDate", "restaurantName", "mealType", "menuDesc")
                .contains(
                        tuple(date, STUDENT_HALL, MealType.BREAKFAST, "라면"),
                        tuple(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥"),
                        tuple(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스"),
                        tuple(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육")
                );
    }

    @Test
    @DisplayName("날짜, 식사종류를 조건으로 조회수가 가장 많은 식사메뉴를 조회(조회수가 같을 경우 추천수 많은 순으로)")
    void findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurant> result = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc(pageable, date, MealType.BREAKFAST);

        // then
        assertThat(result).hasSize(1)
                .extracting("crawlingDate", "restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .contains(
                        tuple(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1)
                );
    }

    @Test
    @DisplayName("날짜, 식사종류를 조건으로 추천수가 가장 많은 식사메뉴를 조회(추천수가 같을 경우 조회수 많은 순으로)")
    void findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 0, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 0, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 1, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<UosRestaurant> result = uosRestaurantRepository.findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc(pageable, date, MealType.BREAKFAST);

        // then
        assertThat(result).hasSize(1)
                .extracting("crawlingDate", "restaurantName", "mealType", "menuDesc", "view", "likeCount")
                .contains(
                        tuple(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 1, 2)
                );
    }

    private UosRestaurant createUosRestaurant(UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
    }

    private UosRestaurant createUosRestaurant(String crawlingDate, UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .crawlingDate(crawlingDate)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
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