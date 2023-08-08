package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.BasicCard;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uos/restaurant")
public class UosRestaurantController {

    private final UosRestaurantService uosRestaurantService;

    @PostMapping("/menu")
    public SkillResponse<SimpleText> getUosRestaurantMenu(SkillPayload payload) {
        UosRestaurantMenuResponse<SimpleText> uosRestaurantMenu = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput());

        return uosRestaurantMenu.toSkillResponse("2.0");
    }
}
