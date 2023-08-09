package seaung.uoscafeteriamenu.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

@Getter
@RequiredArgsConstructor
public enum UosRestaurantName {
    STUDENT_HALL("학생회관 1층"),
    MAIN_BUILDING("본관 8층"),
    WESTERN_RESTAURANT("양식당"),
    MUSEUM_OF_NATURAL_SCIENCE("자연과학관");

    private final String krName;

    // 재정의한 valueOf 메서드
    public static UosRestaurantName fromKrName(String krName) {
        for (UosRestaurantName restaurant : UosRestaurantName.values()) {
            if (restaurant.krName.equals(krName)) {
                return restaurant;
            }
        }
        throw new UosRestaurantMenuException("지원하지 않는 식당 입니다. " + krName);
    }

    // 재정의한 valueOf 메서드
    public static UosRestaurantName fromName(String name) {
        for (UosRestaurantName restaurant : UosRestaurantName.values()) {
            if (restaurant.name().equals(name)) {
                return restaurant;
            }
        }
        throw new UosRestaurantMenuException("지원하지 않는 식당 입니다.");
    }
}
