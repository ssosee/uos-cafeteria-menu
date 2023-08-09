package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Builder;
import lombok.Data;

@Data
public class Outputs {
    private SimpleText simpleText;
//    private SimpleImage simpleImage;
//    private BasicCard basicCard;
//    private TextCard textCard;

    @Builder
    private Outputs(SimpleText simpleText, SimpleImage simpleImage, BasicCard basicCard, TextCard textCard) {
        this.simpleText = simpleText;
//        this.simpleImage = simpleImage;
//        this.basicCard = basicCard;
//        this.textCard = textCard;
    }
}
