package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/simple-text/uos/restaurant")
public class SimpleTextUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;

    @PostMapping("/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput());

        return new ResponseEntity(uosRestaurantMenu.toSkillResponseUseSimpleText(SkillResponse.apiVersion), HttpStatus.OK);
    }
}
