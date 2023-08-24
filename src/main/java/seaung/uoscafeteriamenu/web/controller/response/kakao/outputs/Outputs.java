package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
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

    @Deprecated
    private static Outputs createOutputsUseTextCardWithButton(String text, String blockId, UosRestaurantInput input) {

        Map<String, String> extra = new HashMap<>();
        extra.put("restaurantName", input.getRestaurantName().name());
        extra.put("mealType", input.getMealType().name());

        Button button = Button.createRecommendButton(blockId, extra);

        return Outputs.builder()
                .textCard(new TextCard(text, List.of(button)))
                .build();
    }

    public static Outputs createOutputsUseTextCardWithRecommendButton(UosRestaurantMenuResponse response, String blockId) {

        String restaurantName = UosRestaurantName.fromKrName(response.getRestaurantName()).name();
        String mealType = MealType.fromKrName(response.getMealType()).name();

        Map<String, String> extra = new HashMap<>();
        extra.put("restaurantName", restaurantName);
        extra.put("mealType", mealType);

        Button button = Button.createRecommendButton(blockId, extra);

        return Outputs.builder()
                .textCard(new TextCard(response.getText(), List.of(button)))
                .build();
    }

    public static Outputs createOutputsUseSimpleText(String text) {
        return Outputs.builder()
                .simpleText(new SimpleText(text))
                .build();
    }

    @Deprecated
    // 메뉴를 찾을 수 없거나 학교에서 제공한 메뉴가 없으면 simpleText를 반환한다.
    public static Outputs createOutputs(OutputsDto dto) {
        if(dto.getText().contains(UosRestaurantMenuException.NOT_FOUND_MENU) || dto.getText().contains(CrawlingUtils.NOT_PROVIDED_MENU)) {
            return createOutputsUseSimpleText(dto.getText());
        } else if (dto.getInput() == null && dto.getBlockId() == null) {
            return createOutputsUseSimpleText(dto.getText());
        }
        return createOutputsUseTextCardWithButton(dto.getText(), dto.getBlockId(), dto.getInput());
    }
}
