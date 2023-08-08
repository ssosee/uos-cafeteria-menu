package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Data;

@Data
public class SimpleText {
    private String text;

    public SimpleText(String text) {
        this.text = text;
    }
}
