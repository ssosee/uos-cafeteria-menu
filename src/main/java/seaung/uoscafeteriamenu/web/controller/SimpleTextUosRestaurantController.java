package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/simple-text/uos/restaurant")
public class SimpleTextUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;

    @PostMapping("/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {
        UosRestaurantMenuResponse uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput());

        return new ResponseEntity(uosRestaurantMenu.toSkillResponseUseSimpleText(apiVersion), HttpStatus.OK);
    }

    @PostMapping("/menus")
    public ResponseEntity<SkillResponse> getUosRestaurantsMenu(@RequestBody SkillPayload payload) {
        List<UosRestaurantMenuResponse> uosRestaurantsMenu = uosRestaurantService.getUosRestaurantsMenu(payload.toUosRestaurantsInput());

        return new ResponseEntity(new UosRestaurantsMenuResponse(uosRestaurantsMenu).toSkillResponseUseSimpleText(apiVersion), HttpStatus.OK);
    }
}
