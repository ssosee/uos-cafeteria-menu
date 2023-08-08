package seaung.uoscafeteriamenu.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
        throw new IllegalArgumentException("No enum constant with krName: " + krName);
    }
}
