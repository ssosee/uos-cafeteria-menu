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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    public static String errorMessage = "시스템.. 에러 발생..\n나를 만든 휴.먼이 수리중 이다.\n\n내.친구. 조금만 기다려라..";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<SkillResponse> runtimeException(RuntimeException e) {
        SkillResponse skillResponse = createSimpleTextResponse(errorMessage);

        log.error("[runtimeException 예외 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(ApikeyException.class)
    public ResponseEntity<SkillResponse> apikeyException(ApikeyException e) {
        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage()+" "+errorMessage);

        log.error("[apikeyException 예외 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(UosRestaurantMenuException.class)
    public ResponseEntity<SkillResponse> uosRestaurantMenuException(UosRestaurantMenuException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[uosRestaurantMenuException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<SkillResponse> memberException(MemberException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[memberException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(MenuLikeException.class)
    public ResponseEntity<SkillResponse> menuLikeException(MenuLikeException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[menuLikeException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(RateLimiterException.class)
    public ResponseEntity<SkillResponse> rateLimiterException(RateLimiterException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[rateLimiterException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(WeekendException.class)
    public ResponseEntity<SkillResponse> weekendException(WeekendException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[weekendException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(HolidayException.class)
    public ResponseEntity<SkillResponse> holidayException(HolidayException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[holidayException 발생] ", e);

        // 400으로 보내면 챗봇이 응답을 안준다.
        return new ResponseEntity<>(skillResponse, HttpStatus.OK);
    }

    @ExceptionHandler(SpecialHolidayException.class)
    public ResponseEntity<SkillResponse> specialHolidayException(SpecialHolidayException e) {

        SkillResponse skillResponse = createSimpleTextResponse(e.getMessage());

        log.error("[specialHolidayException 발생] ", e);

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
