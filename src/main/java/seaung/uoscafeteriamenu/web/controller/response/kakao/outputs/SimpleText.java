package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Data;

@Data
public class SimpleText extends Outputs {
    private String text;

    public SimpleText(String text) {
        this.text = text;
    }
}
