package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.util.List;
import java.util.Optional;

public interface UosRestaurantRepository extends JpaRepository<UosRestaurant, Long> {
    Optional<UosRestaurant> findByRestaurantNameAndMealType(UosRestaurantName restaurantName, MealType mealType);
    Optional<UosRestaurant> findByCrawlingDateAndRestaurantNameAndMealType(String crawlingDate, UosRestaurantName restaurantName, MealType mealType);
    List<UosRestaurant> findByCrawlingDateAndAndMealType(String crawlingDate, MealType mealType);
}
