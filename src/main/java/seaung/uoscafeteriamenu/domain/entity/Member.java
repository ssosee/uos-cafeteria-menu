package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx__botUserId", columnList = "botUserId"))
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String botUserId; // 챗봇에서 사용하는 사용자 식별정보

    @OneToMany(mappedBy = "member")
    private List<MenuLike> menuLikes = new ArrayList<>();

    public static Member create(String botUserId) {
        Member member = new Member();
        member.botUserId = botUserId;

        return member;
    }
}
