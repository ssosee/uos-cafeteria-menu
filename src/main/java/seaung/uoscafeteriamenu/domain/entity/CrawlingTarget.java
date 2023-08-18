package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 크롤링 대상 식당 정보
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlingTarget extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private UosRestaurantName restaurantsName; // [학생회관 1층, 본관 8층, 양식당, 자연과학관]
    private String url;
    private String welfareTeamTel;
    private String cssQuery;

    @Builder
    private CrawlingTarget(UosRestaurantName restaurantsName, String url, String welfareTeamTel, String cssQuery) {
        this.restaurantsName = restaurantsName;
        this.url = url;
        this.welfareTeamTel = welfareTeamTel;
        this.cssQuery = cssQuery;
    }
}
