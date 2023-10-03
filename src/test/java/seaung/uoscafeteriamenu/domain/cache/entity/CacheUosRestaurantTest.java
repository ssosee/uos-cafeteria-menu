package seaung.uoscafeteriamenu.domain.cache.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class CacheUosRestaurantTest {
    @Test
    @DisplayName("UosRestaurantInput를 이용해서 CacheUosRestaurant 의 id를 생성한다.")
    void createIdByUosRestaurantInput() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
        UosRestaurantInput input = createUosRestaurantInput(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // when
        String cacheUosRestaurantId = CacheUosRestaurant.createId(input);

        // then
        assertThat(cacheUosRestaurantId).isNotBlank();
    }

    @Test
    @DisplayName("date, UosRestaurantName, MealType 인자를 이용해서 CacheUosRestaurant 의 id를 생성한다.")
    void createIdByArgument() {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());

        // when
        String cacheUosRestaurantId = CacheUosRestaurant.createId(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST);

        // then
        assertThat(cacheUosRestaurantId).isNotBlank();
    }

    private UosRestaurantInput createUosRestaurantInput(String date, UosRestaurantName uosRestaurantName, MealType mealType) {
        return UosRestaurantInput.builder()
                .date(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .build();
    }
}