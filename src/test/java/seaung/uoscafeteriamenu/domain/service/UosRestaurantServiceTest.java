package seaung.uoscafeteriamenu.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantsInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.utils.CrawlingDateUtils;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UosRestaurantServiceTest {

    @Autowired
    UosRestaurantService uosRestaurantService;
    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("학생회관1층 금일 조식 메뉴를 반환한다.")
    void getUosRestaurantMenu() {
        // given
        String date = CrawlingDateUtils.toString(LocalDateTime.now());
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        UosRestaurantInput input = UosRestaurantInput.builder()
                .date(date)
                .restaurantName(UosRestaurantName.STUDENT_HALL)
                .mealType(MealType.BREAKFAST)
                .build();

        // when
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(input);

        // then
        assertAll(
                () -> assertThat(uosRestaurantMenu.getMenu()).isEqualTo("라면"),
                () -> assertThat(uosRestaurantMenu.getRestaurantName()).isEqualTo("학생회관 1층"),
                () -> assertThat(uosRestaurantMenu.getMealType()).isEqualTo("조식")
        );
    }

    @Test
    @DisplayName("학생회관1층 메뉴가 없을 경우 예외가 발생한다.")
    void getUosRestaurantMenuUosRestaurantMenuException() {
        // given
        UosRestaurantInput input = UosRestaurantInput.builder()
                .date(CrawlingDateUtils.toString(LocalDateTime.now()))
                .restaurantName(UosRestaurantName.STUDENT_HALL)
                .mealType(MealType.BREAKFAST)
                .build();

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.getUosRestaurantMenu(input))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_MENU);
    }

    @Test
    @DisplayName("금일 조식 메뉴들을 조회한다.")
    void getUosRestaurantMenus() {
        // given
        String date = CrawlingDateUtils.toString(LocalDateTime.now());

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, UosRestaurantName.MAIN_BUILDING, MealType.BREAKFAST, "김밥");
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스");
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육");
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        UosRestaurantsInput input = createUosRestaurantsInput(date, MealType.BREAKFAST);

        // when
        List<UosRestaurantMenuResponse> uosRestaurantMenus = uosRestaurantService.getUosRestaurantsMenu(input);

        // then
        assertThat(uosRestaurantMenus).hasSize(4)
                .extracting("restaurantName", "mealType", "menu")
                .contains(
                        tuple(UosRestaurantName.STUDENT_HALL.getKrName(), MealType.BREAKFAST.getKrName(), "라면"),
                        tuple(UosRestaurantName.MAIN_BUILDING.getKrName(), MealType.BREAKFAST.getKrName(), "김밥"),
                        tuple(UosRestaurantName.WESTERN_RESTAURANT.getKrName(), MealType.BREAKFAST.getKrName(), "돈까스"),
                        tuple(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(), MealType.BREAKFAST.getKrName(), "제육")
                );

    }

    private static UosRestaurantsInput createUosRestaurantsInput(String date, MealType mealType) {
        return UosRestaurantsInput.builder()
                .date(date)
                .mealType(mealType)
                .build();
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
    }
}