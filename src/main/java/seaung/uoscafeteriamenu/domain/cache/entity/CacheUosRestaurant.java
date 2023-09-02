package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.io.Serializable;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityManager.DEFAULT_TTL;

@Getter
@RedisHash(value = "cacheUosRestaurant", timeToLive = 86400)
public class CacheUosRestaurant implements Serializable {
    @Id
    private UosRestaurantName restaurantName;
    private MealType mealType;
    private String menu;
    private Integer view;
    private Integer likeCount;
    @TimeToLive
    private int expiration;

    public static CacheUosRestaurant create(UosRestaurantName restaurantName, MealType mealType, String menu, Integer view, Integer likeCount, int expiration) {
        CacheUosRestaurant cacheUosRestaurant = new CacheUosRestaurant();
        cacheUosRestaurant.restaurantName = restaurantName;
        cacheUosRestaurant.mealType = mealType;
        cacheUosRestaurant.menu = menu;
        cacheUosRestaurant.view = view;
        cacheUosRestaurant.likeCount = likeCount;
        cacheUosRestaurant.expiration = expiration;

        return cacheUosRestaurant;
    }

    public static CacheUosRestaurant of(UosRestaurant uosRestaurant) {
        CacheUosRestaurant cacheUosRestaurant = new CacheUosRestaurant();
        cacheUosRestaurant.restaurantName = uosRestaurant.getRestaurantName();
        cacheUosRestaurant.mealType = uosRestaurant.getMealType();
        cacheUosRestaurant.menu = uosRestaurant.getMenuDesc();
        cacheUosRestaurant.view = uosRestaurant.getView();
        cacheUosRestaurant.likeCount = uosRestaurant.getLikeCount();
        cacheUosRestaurant.expiration = DEFAULT_TTL;

        return cacheUosRestaurant;
    }
}
