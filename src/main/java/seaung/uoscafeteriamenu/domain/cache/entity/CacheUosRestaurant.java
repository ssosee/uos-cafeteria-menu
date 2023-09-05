package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.io.Serializable;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityConfig.DEFAULT_TTL;

@Getter
@RedisHash(value = "cacheUosRestaurant", timeToLive = DEFAULT_TTL)
@NoArgsConstructor
public class CacheUosRestaurant implements Serializable {
    @Id
    private UosRestaurantName restaurantName;
    private MealType mealType;
    private String menu;
    private Integer view;
    private Integer likeCount;
    @TimeToLive
    private int expiration;

    @Builder
    private CacheUosRestaurant(UosRestaurantName restaurantName, MealType mealType, String menu, Integer view, Integer likeCount, int expiration) {
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menu = menu;
        this.view = view;
        this.likeCount = likeCount;
        this.expiration = expiration;
    }

    public static CacheUosRestaurant create(UosRestaurantName restaurantName, MealType mealType, String menu, Integer view, Integer likeCount, int expiration) {
        return CacheUosRestaurant.builder()
                .restaurantName(restaurantName)
                .mealType(mealType)
                .menu(menu)
                .view(view)
                .likeCount(likeCount)
                .expiration(expiration)
                .build();
    }

    public static CacheUosRestaurant of(UosRestaurant uosRestaurant) {
        return CacheUosRestaurant.builder()
                .restaurantName(uosRestaurant.getRestaurantName())
                .mealType(uosRestaurant.getMealType())
                .menu(uosRestaurant.getMenuDesc())
                .view(uosRestaurant.getView())
                .likeCount(uosRestaurant.getLikeCount())
                .expiration(DEFAULT_TTL)
                .build();
    }
}
