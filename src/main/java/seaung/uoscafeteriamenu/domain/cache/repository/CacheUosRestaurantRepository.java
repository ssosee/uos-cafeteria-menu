package seaung.uoscafeteriamenu.domain.cache.repository;

import org.springframework.data.repository.CrudRepository;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.MealType;

import java.util.List;

public interface CacheUosRestaurantRepository extends CrudRepository<CacheUosRestaurant, String> {

    List<CacheUosRestaurant> findByDateAndMealType(String date, MealType mealType);
}
