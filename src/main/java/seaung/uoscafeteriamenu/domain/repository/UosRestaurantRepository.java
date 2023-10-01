package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.util.List;
import java.util.Optional;

public interface UosRestaurantRepository extends JpaRepository<UosRestaurant, Long> {
    Optional<UosRestaurant> findByRestaurantNameAndMealType(UosRestaurantName restaurantName, MealType mealType);

    Optional<UosRestaurant> findByCrawlingDateAndRestaurantNameAndMealType(String crawlingDate, UosRestaurantName restaurantName, MealType mealType);

    List<UosRestaurant> findByCrawlingDateAndMealType(String crawlingDate, MealType mealType);

    // 조회수가 가장 많은 식사메뉴를 조회(조회수가 같을 경우 추천수 많은 순으로)
    Page<UosRestaurant> findByCrawlingDateAndMealTypeOrderByViewDescLikeCountDesc(Pageable pageable, String crawlingDate, MealType mealType);

    // 추천수가 가장 많은 식사메뉴 조회(추천수가 같을 경우 조회수 많은 순으로)
    Page<UosRestaurant> findByCrawlingDateAndMealTypeOrderByLikeCountDescViewDesc(Pageable pageable, String crawlingDate, MealType mealType);
    List<UosRestaurant> findByCrawlingDate(String date);
}
