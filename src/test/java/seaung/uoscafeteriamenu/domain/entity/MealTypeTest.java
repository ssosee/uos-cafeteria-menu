package seaung.uoscafeteriamenu.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class MealTypeTest {
    @Test
    @DisplayName("영어로 MealType enum 타입을 찾는다.")
    void fromName() {
        // given
        String mealTypeName = MealType.BREAKFAST.name();

        // when
        MealType mealType = MealType.fromName(mealTypeName);

        // then
        assertThat(mealTypeName).isEqualTo(mealType.name());
    }

    @Test
    @DisplayName("지원하지 않는 영어로 MealType enum 타입을 찾으면 예외가 발생한다.")
    void fromNameException() {
        // given
        String mealTypeName = "LUNCH!";

        // when // then
        assertThatThrownBy(() -> MealType.fromName(mealTypeName))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_MEAL_TYPE);
    }

    @Test
    @DisplayName("한글로 MealType enum 타입을 찾는다.")
    void fromKrName() {
        // given
        String mealTypeName = MealType.BREAKFAST.getKrName();

        // when
        MealType mealType = MealType.fromKrName(mealTypeName);

        // then
        assertThat(mealTypeName).isEqualTo(mealType.getKrName());
    }

    @Test
    @DisplayName("지원하지 않는 한글로 MealType enum 타입을 찾으면 예외가 발생한다.")
    void fromKrNameException() {
        // given
        String mealTypeName = "점심";

        // when // then
        assertThatThrownBy(() -> MealType.fromName(mealTypeName))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_MEAL_TYPE);
    }
}