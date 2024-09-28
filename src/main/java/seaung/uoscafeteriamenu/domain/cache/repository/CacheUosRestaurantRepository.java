package seaung.uoscafeteriamenu.domain.cache.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

public interface CacheUosRestaurantRepository extends CrudRepository<CacheUosRestaurant, String> {

    List<CacheUosRestaurant> findByDateAndMealType(String date, MealType mealType);
    Optional<CacheUosRestaurant> findByDateAndRestaurantNameAndMealType(String date, UosRestaurantName restaurantName, MealType mealType);
    List<CacheUosRestaurant> findByDate(String date);
}
