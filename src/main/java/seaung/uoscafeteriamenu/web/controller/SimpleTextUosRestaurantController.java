package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantsMenuResponse;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/simple-text/uos/restaurant")
public class SimpleTextUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;

    // 식당이름, 식사종류로 금일 식당 메뉴 조회
    @PostMapping("/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput());

        log.info("request={}", payload);

        return new ResponseEntity(uosRestaurantMenu.toSkillResponseUseSimpleText(apiVersion), HttpStatus.OK);
    }

    // 식사종류로 금익 식당 메뉴 조회
    @PostMapping("/menus")
    public ResponseEntity<SkillResponse> getUosRestaurantsMenu(@RequestBody SkillPayload payload) {
        List<UosRestaurantMenuResponse> uosRestaurantsMenu = uosRestaurantService.getUosRestaurantsMenu(payload.toUosRestaurantsInput());
        UosRestaurantsMenuResponse response = new UosRestaurantsMenuResponse(uosRestaurantsMenu);

        log.info("request={}", payload);

        return new ResponseEntity(response.toSkillResponseUseSimpleText(apiVersion), HttpStatus.OK);
    }
}
