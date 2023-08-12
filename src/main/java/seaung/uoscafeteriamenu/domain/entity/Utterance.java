package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx__content__member_id", columnList = "content, member_id"))
public class Utterance extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String blockId; // 블록 아이디
    private String blockName; // 블록 이름
    private String content; // 발화 내용

    @Column(columnDefinition = "char", length = 2)
    @Enumerated(EnumType.STRING)
    private Language lang; // ISO 639-1를 따른다고 가정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // OneToOne은 연관관계 주인이 아닌곳에서 호출할 경우 lazy 안됨
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private Action action;
}
