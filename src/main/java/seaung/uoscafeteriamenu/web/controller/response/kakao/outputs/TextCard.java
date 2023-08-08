package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;

import java.util.List;

@Data
public class TextCard extends Outputs {
    private String text;
    private List<Button> buttons;
}
