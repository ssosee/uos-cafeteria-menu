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
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

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
    @DisplayName("학생회관1층 조식 메뉴정보를 반환한다.")
    void getUosRestaurantMenu() {
        // given
        UosRestaurant uosRestaurant = createUosRestaurant(UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        UosRestaurantInput input = UosRestaurantInput.builder()
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
    @DisplayName("학생회관1층 메뉴 정보가 없을 경우 예외가 발생한다.")
    void getUosRestaurantMenuUosRestaurantMenuException() {
        // given
        UosRestaurantInput input = UosRestaurantInput.builder()
                .restaurantName(UosRestaurantName.STUDENT_HALL)
                .mealType(MealType.BREAKFAST)
                .build();

        // when // then
        assertThatThrownBy(() -> uosRestaurantService.getUosRestaurantMenu(input))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_MENU);
    }

    private UosRestaurant createUosRestaurant(UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
    }
}