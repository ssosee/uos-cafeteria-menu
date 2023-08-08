package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;

/**
 * 사용자의 발화를 받은 봇의 정보를 담고 있는 필드입니다.
 */
@Data
public class Bot {
    private String id; // 봇 고유 식별자
    private String name; // 설정된 봇의 이름

    @Builder
    private Bot(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
