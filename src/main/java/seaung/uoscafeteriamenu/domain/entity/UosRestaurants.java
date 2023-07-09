package seaung.uoscafeteriamenu.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 크롤링 대상 식당 정보
 */
@Entity
@Getter
public class UosRestaurants extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String restaurantsName;
    private String url;
    private String welfareTeamTel;
}
