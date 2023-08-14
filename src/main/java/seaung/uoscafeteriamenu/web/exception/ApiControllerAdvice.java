package seaung.uoscafeteriamenu.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillTemplate;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.SimpleText;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(UosRestaurantMenuException.class)
    public ResponseEntity<SkillResponse> uosRestaurantMenuException(UosRestaurantMenuException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[학교식당 메뉴 조회 예외 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<SkillResponse> memberException(MemberException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[회원 예외 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MenuLikeException.class)
    public ResponseEntity<SkillResponse> menuLikeException(MenuLikeException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[메뉴 추천이력 예외 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    private SkillResponse createSimpleTextResponse(String errorMessage) {
        Outputs outputs = Outputs.builder()
                .simpleText(new SimpleText(errorMessage))
                .build();

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(SkillResponse.apiVersion)
                .template(template)
                .build();
    }
}
