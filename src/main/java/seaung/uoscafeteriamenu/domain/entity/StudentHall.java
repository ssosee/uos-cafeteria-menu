package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 학생회관 1층
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentHall extends Menu {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
