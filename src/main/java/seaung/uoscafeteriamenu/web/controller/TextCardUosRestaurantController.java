package seaung.uoscafeteriamenu.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantsMenuResponse;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.controller.request.kakao.SkillPayload;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

import static seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse.apiVersion;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/text-card/uos/restaurant")
public class TextCardUosRestaurantController {

    private final UosRestaurantService uosRestaurantService;
    private final TimeProvider timeProvider;
    private final String bockId = "64d75083c800862a54172c4a"; // 추천하기 블록 아이디

    /**
     * 식당이름, 식사종류로 금일 식당 메뉴 조회
     */
    @PostMapping("/menu")
    public ResponseEntity<SkillResponse> getUosRestaurantMenu(@RequestBody SkillPayload payload) {

        UosRestaurantMenuResponse response = uosRestaurantService.getUosRestaurantMenu(payload.toUosRestaurantInput(timeProvider));

        return new ResponseEntity<>(response.toSkillResponseUseTextCard(apiVersion, bockId, payload.toUosRestaurantInput(timeProvider)), HttpStatus.OK);
    }

    /**
     * 메뉴 추천
     */
    @PostMapping("/menu/recommend")
    public ResponseEntity<SkillResponse> recommendUosRestaurantMenu(@RequestBody SkillPayload payload) {

        String response = uosRestaurantService.recommendUosRestaurantMenu(payload.toUosRestaurantInputUseActionClientExtra(timeProvider));

        return new ResponseEntity<>(SkillResponse.createSkillResponseUseSimpleText(apiVersion, response), HttpStatus.OK);
    }

    /**
     * 인기 메뉴 조회
     * 현재 시간대의 식사종류와 일치하는 가장 조회수가 많은 금일 식사 메뉴 조회
     */
    @PostMapping("/menu/top1-view")
    public ResponseEntity<SkillResponse> getTop1UosRestaurantMenuByView(@RequestBody SkillPayload payload,
                                                                        @PageableDefault(page = 0, size = 1) Pageable pageable) {

        Page<UosRestaurantMenuResponse> top1UosRestaurantMenuByView
                = uosRestaurantService.findTop1UosRestaurantMenuByView(pageable, timeProvider.getCurrentLocalDateTime());
        UosRestaurantsMenuResponse response = new UosRestaurantsMenuResponse(top1UosRestaurantMenuByView.getContent());

        return new ResponseEntity<>(response.toSkillResponseUseTextCard(apiVersion, bockId), HttpStatus.OK);
    }

    /**
     * 추천 메뉴 조회
     * 현재 시간대의 식사종류와 일치하는 가장 추천수가 많은 금일 식사 메뉴 조회
     */
    @PostMapping("/menu/top1-like")
    public ResponseEntity<SkillResponse> getTop1UosRestaurantMenuByLikeCount(@RequestBody SkillPayload payload,
                                                                        @PageableDefault(page = 0, size = 1) Pageable pageable) {

        Page<UosRestaurantMenuResponse> top1UosRestaurantMenuByView
                = uosRestaurantService.findTop1UosRestaurantMenuByLikeCount(pageable, timeProvider.getCurrentLocalDateTime());
        UosRestaurantsMenuResponse response = new UosRestaurantsMenuResponse(top1UosRestaurantMenuByView.getContent());

        return new ResponseEntity<>(response.toSkillResponseUseTextCard(apiVersion, bockId), HttpStatus.OK);
    }
}
