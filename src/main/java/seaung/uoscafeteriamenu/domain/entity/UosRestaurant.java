package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx__restaurantName__mealType__crawlingDate", columnList = "restaurantName, mealType, crawlingDate"))
public class UosRestaurant extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UosRestaurantName restaurantName;
    private String crawlingDate; // 크롤링 대상에 명시되어있는 날짜
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    @Column(columnDefinition = "varchar(1200)")
    private String menuDesc;
    private Integer view;
    private Integer likeCount;

    @OneToMany(mappedBy = "uosRestaurant")
    private List<MenuLike> menuLikes = new ArrayList<>();

    @Builder
    private UosRestaurant(UosRestaurantName restaurantName, String crawlingDate, MealType mealType, String menuDesc, Integer view, Integer likeCount) {
        this.restaurantName = restaurantName;
        this.crawlingDate = crawlingDate;
        this.mealType = mealType;
        this.menuDesc = menuDesc;
        this.view = view;
        this.likeCount = likeCount;
    }

    // 조회수 증가
    public void increaseView() {
        this.view++;
    }

    // 추천수 증가
    public void increaseLikeCount() {
        this.likeCount++;
    }
}
