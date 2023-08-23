package seaung.uoscafeteriamenu.web.controller.response.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button {
    private String label; // 버튼에 적히는 문구입니다.
    private String action; // 버튼 클릭시 수행될 작업입니다.
    private String webLinkUrl; // 웹 브라우저를 열고 webLinkUrl 의 주소로 이동합니다.

    // message: 사용자의 발화로 messageText를 내보냅니다. (바로가기 응답의 메세지 연결 기능과 동일)
    // block: 블록 연결시 사용자의 발화로 노출됩니다.
    private String messageText;
    private String phoneNumber; // phoneNumber에 있는 번호로 전화를 겁니다.
    private String blockId; // blockId를 갖는 블록을 호출합니다. (바로가기 응답의 블록 연결 기능과 동일)
    private Map<String, String> extra; // block이나 message action으로 블록 호출시, 스킬 서버에 추가적으로 제공하는 정보

    @Builder
    private Button(String label, String action, String webLinkUrl, String messageText, String phoneNumber, String blockId, Map<String, String> extra) {
        this.label = label;
        this.action = action;
        this.webLinkUrl = webLinkUrl;
        this.messageText = messageText;
        this.phoneNumber = phoneNumber;
        this.blockId = blockId;
        this.extra = extra;
    }

    public static Button createRecommendButton(String blockId, Map<String, String > extra) {
        return Button.builder()
                .label("추천 😋")
                .action("block")
                .blockId(blockId)
                .extra(extra)
                .build();
    }
}
