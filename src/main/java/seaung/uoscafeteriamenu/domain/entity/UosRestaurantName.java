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
}
