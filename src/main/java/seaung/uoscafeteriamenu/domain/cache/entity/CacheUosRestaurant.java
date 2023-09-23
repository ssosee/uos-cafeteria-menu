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
    private String id;
    private UosRestaurantName restaurantName;
    private String date;
    private MealType mealType;
    private String menuDesc;
    private Integer view;
    private Integer likeCount;
    @TimeToLive
    private int expiration;

    @Builder
    private CacheUosRestaurant(String id, String date, UosRestaurantName restaurantName, MealType mealType, String menuDesc, Integer view, Integer likeCount, int expiration) {
        this.id = id;
        this.date = date;
        this.restaurantName = restaurantName;
        this.mealType = mealType;
        this.menuDesc = menuDesc;
        this.view = view;
        this.likeCount = likeCount;
        this.expiration = expiration;
    }

    public static CacheUosRestaurant create(String date, UosRestaurantName restaurantName, MealType mealType, String menuDesc, Integer view, Integer likeCount, int expiration) {
        return CacheUosRestaurant.builder()
                .id(createId(date, restaurantName, mealType))
                .date(date)
                .restaurantName(restaurantName)
                .mealType(mealType)
                .menuDesc(menuDesc)
                .view(view)
                .likeCount(likeCount)
                .expiration(expiration)
                .build();
    }

    public static CacheUosRestaurant of(UosRestaurant uosRestaurant) {
        return CacheUosRestaurant.builder()
                .id(createId(uosRestaurant.getCrawlingDate(), uosRestaurant.getRestaurantName(), uosRestaurant.getMealType()))
                .date(uosRestaurant.getCrawlingDate())
                .restaurantName(uosRestaurant.getRestaurantName())
                .mealType(uosRestaurant.getMealType())
                .menuDesc(uosRestaurant.getMenuDesc())
                .view(uosRestaurant.getView())
                .likeCount(uosRestaurant.getLikeCount())
                .expiration(DEFAULT_TTL)
                .build();
    }

    private static String createId(String date, UosRestaurantName uosRestaurantName, MealType mealType) {

        StringBuilder sb = new StringBuilder();
        sb.append(date).append("-");
        sb.append(uosRestaurantName).append("-");
        sb.append(mealType).append("-");

        return sb.toString();
    }
}
