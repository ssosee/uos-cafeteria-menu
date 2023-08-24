package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantsMenuResponse;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.converter.UosRestaurantServiceResponseConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/simple-text/uos")
public class SimpleTextUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;
    private final TimeProvider timeProvider;
    private final UosRestaurantServiceResponseConverter uosRestaurantServiceResponseConverter;

    /**
     * 식당이름, 식사종류로 금일 식당 메뉴 조회
     * 추천을 받기 위해 textCard를 사용해야함.
     * @param payload
     * @return
     * @Deprecated
     */
    @Deprecated
    @PostMapping("/restaurant/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {
        UosRestaurantMenuResponse response = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput(timeProvider));

        return new ResponseEntity<>(response.toSkillResponseUseSimpleText(apiVersion), HttpStatus.OK);
    }

    /**
     * 식사 종류로 금일 식당 메뉴 조회
     */
    @PostMapping("/restaurants/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantsMenu(@RequestBody SkillPayload payload) {

        // 식사 종류로 금일 식당 메뉴 조회
        List<UosRestaurantMenuResponse> uosRestaurantsMenu = uosRestaurantService
                .getUosRestaurantsMenu(payload.toUosRestaurantsInput(timeProvider));

        // 카카오톡 응답으로 변경
        SkillResponse response = uosRestaurantServiceResponseConverter.toSkillResponseUseSimpleText(apiVersion, uosRestaurantsMenu);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
