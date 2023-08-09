package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Profile;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Thumbnail;

import java.util.List;

@Data
public class CommerceCard {
    private String title;
    private String description;
    private int price;
    private String currency;
    private int discount;
    private int discountRate;
    private int dicountedPrice;
    private List<Thumbnail> thumbnails;
    private Profile profile;
    private List<Button> buttons;
}
