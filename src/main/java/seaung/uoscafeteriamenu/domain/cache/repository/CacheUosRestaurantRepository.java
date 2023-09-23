package seaung.uoscafeteriamenu.domain.cache.repository;

import org.springframework.data.repository.CrudRepository;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheUosRestaurant;

import java.util.Optional;

public interface CacheUosRestaurantRepository extends CrudRepository<CacheUosRestaurant, String> {
}
