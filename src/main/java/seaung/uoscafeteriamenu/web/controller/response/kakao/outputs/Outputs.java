package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.web.controller.response.kakao.Button;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 인 필드는 제외
public class Outputs {
    private SimpleText simpleText;
    private SimpleImage simpleImage;
    private BasicCard basicCard;
    private TextCard textCard;

    @Builder
    private Outputs(SimpleText simpleText, SimpleImage simpleImage, BasicCard basicCard, TextCard textCard) {
        this.simpleText = simpleText;
        this.simpleImage = simpleImage;
        this.basicCard = basicCard;
        this.textCard = textCard;
    }

    private static Outputs createOutputsUseTextCard(String text, String blockId, UosRestaurantInput input) {

        Map<String, String> extra = new HashMap<>();
        extra.put("restaurantName", input.getRestaurantName().name());
        extra.put("mealType", input.getMealType().name());

        Button button = Button.builder()
                .label("추천 😋")
                .action("block")
                .blockId(blockId)
                .extra(extra)
                .build();

        return Outputs.builder()
                .textCard(new TextCard(text, List.of(button)))
                .build();
    }

    private static Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }

    // 메뉴를 찾을 수 없거나 학교에서 제공한 메뉴가 없으면 simpleText를 반환한다.
    public static Outputs findOutputs(OutputsDto dto) {
        if(dto.getText().contains(UosRestaurantMenuException.NOT_FOUND_MENU) || dto.getText().contains(CrawlingUtils.NOT_PROVIDED_MENU)) {
            return createOutputsUseSimpleText(dto.getText());
        } else if (dto.getInput() == null && dto.getBlockId() == null) {
            return createOutputsUseSimpleText(dto.getText());
        }
        return createOutputsUseTextCard(dto.getText(), dto.getBlockId(), dto.getInput());
    }
}
