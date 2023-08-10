package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx__restaurantName__mealType__crawlingDate", columnList = "restaurantName, mealType, crawlingDate"))
public class UosRestaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UosRestaurantName restaurantName;
    private String crawlingDate; // 크롤링 대상에 명시되어있는 날짜
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @Lob
    private String menuDesc;

    @Builder
    private UosRestaurant(UosRestaurantName restaurantName, String crawlingDate, MealType mealType, String menuDesc) {
        this.restaurantName = restaurantName;
        this.crawlingDate = crawlingDate;
        this.mealType = mealType;
        this.menuDesc = menuDesc;
    }
}
