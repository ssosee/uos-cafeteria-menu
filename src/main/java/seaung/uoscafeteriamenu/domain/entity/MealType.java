package seaung.uoscafeteriamenu.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalTime;

import static seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException.NOT_FOUND_MEAL_TYPE;

@Getter
@RequiredArgsConstructor
public enum MealType {
    BREAKFAST("조식"),
    LUNCH("중식"),
    DINNER("석식");

    private final String krName;

    // 재정의한 valueOf 메서드
    public static MealType fromKrName(String krName) {
        for (MealType mealType : MealType.values()) {
            if (mealType.krName.equals(krName)) return mealType;
        }
        throw new UosRestaurantMenuException(NOT_FOUND_MEAL_TYPE);
    }

    // 재정의한 valueOf 메서드
    public static MealType fromName(String name) {
        for (MealType mealType : MealType.values()) {
            if (mealType.name().equals(name)) return mealType;
        }
        throw new UosRestaurantMenuException(NOT_FOUND_MEAL_TYPE);
    }
}
