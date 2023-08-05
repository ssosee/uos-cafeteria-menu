package seaung.uoscafeteriamenu.domain.entity;

import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public abstract class Menu extends BaseTimeEntity {
    private String crawlingDate;
    @Enumerated(EnumType.STRING)
    private MealType mealType;
    // private String operatingTime;
    @Lob
    private String menuDesc;
}
