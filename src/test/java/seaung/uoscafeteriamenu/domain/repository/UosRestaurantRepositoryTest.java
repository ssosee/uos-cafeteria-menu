package seaung.uoscafeteriamenu.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.utils.CrawlingDateUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UosRestaurantRepositoryTest {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("식당이름, 식사종류를 이용하여 식당 메뉴를 조회한다.")
    void findByRestaurantNameAndMealType() {
        // given
        UosRestaurant uosRestaurant = createUosRestaurant(UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        // when
        UosRestaurant result = uosRestaurantRepository.findByRestaurantNameAndMealType(UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST).get();

        // then
        assertAll(
                () -> assertThat(result.getRestaurantName()).isEqualTo(UosRestaurantName.STUDENT_HALL),
                () -> assertThat(result.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(result.getMenuDesc()).isEqualTo("라면")
        );
    }

    @Test
    @DisplayName("날짜, 식당이름, 식사종류를 이용하여 식당 메뉴를 조회한다.")
    void findByCrawlingDateAndRestaurantNameAndMealType() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String date = CrawlingDateUtils.toString(now);

        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        // when
        UosRestaurant result = uosRestaurantRepository.findByCrawlingDateAndRestaurantNameAndMealType(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST).get();

        // then
        assertAll(
                () -> assertThat(result.getCrawlingDate()).isEqualTo(date),
                () -> assertThat(result.getRestaurantName()).isEqualTo(UosRestaurantName.STUDENT_HALL),
                () -> assertThat(result.getMealType()).isEqualTo(MealType.BREAKFAST),
                () -> assertThat(result.getMenuDesc()).isEqualTo("라면")
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
}