package seaung.uoscafeteriamenu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuLike extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uos_restaurant_id")
    private UosRestaurant uosRestaurant;

    public static MenuLike create(Member member, UosRestaurant uosRestaurant) {
        MenuLike menuLike = new MenuLike();
        menuLike.changeMember(member);
        menuLike.changeUosRestaurant(uosRestaurant);

        return menuLike;
    }

    private void changeMember(Member member) {
        this.member = member;
        member.getMenuLikes().add(this);
    }

    private void changeUosRestaurant(UosRestaurant uosRestaurant) {
        this.uosRestaurant = uosRestaurant;
        uosRestaurant.getMenuLikes().add(this);
    }
}
