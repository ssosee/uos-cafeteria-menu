package seaung.uoscafeteriamenu.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class UosRestaurantNameTest {

    @Test
    @DisplayName("영어로 UosRestaurantName enum 타입을 찾는다.")
    void fromName() {
        // given
        String studentHallName = UosRestaurantName.STUDENT_HALL.name();

        // when
        UosRestaurantName uosRestaurantName = UosRestaurantName.fromName(studentHallName);

        // then
        assertThat(studentHallName).isEqualTo(uosRestaurantName.name());
    }

    @Test
    @DisplayName("지원하지 않는 영어로 UosRestaurantName enum 타입을 찾으면 예외가 발생한다.")
    void fromNameException() {
        // given
        String restaurantName = "HELLO";

        // when // then
        assertThatThrownBy(() -> UosRestaurantName.fromName(restaurantName))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_RESTAURANT);
    }

    @Test
    @DisplayName("한글로 UosRestaurantName enum 타입을 찾는다.")
    void fromKrName() {
        // given
        String studentHallKrName = UosRestaurantName.STUDENT_HALL.getKrName();

        // when
        UosRestaurantName uosRestaurantName = UosRestaurantName.fromKrName(studentHallKrName);

        // then
        assertThat(studentHallKrName).isEqualTo(uosRestaurantName.getKrName());
    }

    @Test
    @DisplayName("지원하지 않는 한글로 UosRestaurantName enum 타입을 찾으면 예외가 발생한다.")
    void fromKrNameException() {
        // given
        String restaurantName = "안녕";

        // when // then
        assertThatThrownBy(() -> UosRestaurantName.fromKrName(restaurantName))
                .isInstanceOf(UosRestaurantMenuException.class)
                .hasMessage(UosRestaurantMenuException.NOT_FOUND_RESTAURANT);
    }
}