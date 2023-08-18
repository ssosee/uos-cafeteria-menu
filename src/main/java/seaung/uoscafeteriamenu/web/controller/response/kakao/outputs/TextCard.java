package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TextCard {
    private String text;
    private List<Button> buttons;

    public TextCard(String text) {
        this.text = text;
    }

    public TextCard(String text, List<Button> buttons) {
        this.text = text;
        this.buttons = buttons;
    }
}
