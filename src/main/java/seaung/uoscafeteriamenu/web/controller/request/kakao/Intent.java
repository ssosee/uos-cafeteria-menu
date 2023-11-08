package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 발화와 일치하는 블록의 정보를 담고 있는 필드입니다.
 * 발화가 지식+에 일치하는 경우, 일치하는 지식의 목록을 포함합니다.
 */
@Data
@NoArgsConstructor
public class Intent {
    private String id;
    private String name;
    private Extra extra;

    @Builder
    private Intent(String id, String name, Extra extra) {
        this.id = id;
        this.name = name;
        this.extra = extra;
    }
}
