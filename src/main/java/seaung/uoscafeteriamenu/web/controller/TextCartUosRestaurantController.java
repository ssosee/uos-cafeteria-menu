package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

import static seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse.apiVersion;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-card/uos/restaurant")
public class TextCartUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;

    // 식당이름, 식사종류로 금일 식당 메뉴 조회
    @PostMapping("/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput());

        log.info("request={}", payload);

        return new ResponseEntity(uosRestaurantMenu.toSkillResponseUseTextCard(apiVersion), HttpStatus.OK);
    }
}
