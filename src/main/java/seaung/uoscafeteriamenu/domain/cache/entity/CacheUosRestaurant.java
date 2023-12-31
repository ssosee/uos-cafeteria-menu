package seaung.uoscafeteriamenu.domain.cache.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityConfig.DEFAULT_TTL;

/**
 * https://dkswnkk.tistory.com/709
 * @Indexed로 생성된 보조 인덱스는 해당 데이터의 TTL이 만료되더라도 자동으로 삭제가 되지 않는다.
 */
@Getter
@RedisHash(value = "cacheUosRestaurant", timeToLive = DEFAULT_TTL)
@NoArgsConstructor
public class CacheUosRestaurant implements Serializable {
    @Id
    private String id;
    @Indexed
    private UosRestaurantName restaurantName;
    @Indexed
    private String date;
    @Indexed
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

    public static CacheUosRestaurant create(Long id, String date, UosRestaurantName restaurantName, MealType mealType, String menuDesc, Integer view, Integer likeCount, int expiration) {
        return CacheUosRestaurant.builder()
                .id(id.toString())
                //.id(createId(date, restaurantName, mealType))
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
                .id(uosRestaurant.getId().toString())
                //.id(createId(uosRestaurant.getCrawlingDate(), uosRestaurant.getRestaurantName(), uosRestaurant.getMealType()))
                .date(uosRestaurant.getCrawlingDate())
                .restaurantName(uosRestaurant.getRestaurantName())
                .mealType(uosRestaurant.getMealType())
                .menuDesc(uosRestaurant.getMenuDesc())
                .view(uosRestaurant.getView())
                .likeCount(uosRestaurant.getLikeCount())
                .expiration(DEFAULT_TTL)
                .build();
    }

    public static List<CacheUosRestaurant> ofList(List<UosRestaurant> uosRestaurants) {
        return uosRestaurants.stream()
                .map(CacheUosRestaurant::of)
                .collect(Collectors.toList());
    }

    public static String createId(String date, UosRestaurantName uosRestaurantName, MealType mealType) {

        StringBuilder sb = new StringBuilder();
        sb.append(date).append("-");
        sb.append(uosRestaurantName).append("-");
        sb.append(mealType);

        return sb.toString();
    }

    public static String createId(UosRestaurantInput input) {

        StringBuilder sb = new StringBuilder();
        sb.append(input.getDate()).append("-");
        sb.append(input.getRestaurantName()).append("-");
        sb.append(input.getMealType());

        return sb.toString();
    }

    public void increaseView() {
        this.view++;
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }
}
