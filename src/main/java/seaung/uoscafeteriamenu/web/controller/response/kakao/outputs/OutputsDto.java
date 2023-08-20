package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;

@Data
public class OutputsDto {
    private String text;
    private String blockId;
    private UosRestaurantInput input;

    @Builder
    private OutputsDto(String text, String blockId, UosRestaurantInput input) {
        this.text = text;
        this.blockId = blockId;
        this.input = input;
    }
}