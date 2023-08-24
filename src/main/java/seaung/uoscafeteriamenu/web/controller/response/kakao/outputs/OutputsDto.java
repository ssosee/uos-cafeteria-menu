package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;

@Data
public class OutputsDto {
    private String text;
    private String blockId;
    private UosRestaurantMenuResponse response;
    private UosRestaurantInput input;

    @Builder
    private OutputsDto(String text, String blockId, UosRestaurantMenuResponse response, UosRestaurantInput input) {
        this.text = text;
        this.blockId = blockId;
        this.response = response;
        this.input = input;
    }

    public static OutputsDto createOutputDto(String blockId, UosRestaurantMenuResponse response) {
        return OutputsDto.builder()
                .blockId(blockId)
                .response(response)
                .build();
    }

    public static OutputsDto createOutputDto(String text, String blockId, UosRestaurantInput input) {
        return OutputsDto.builder()
                .text(text)
                .blockId(blockId)
                .input(input)
                .build();
    }
}
