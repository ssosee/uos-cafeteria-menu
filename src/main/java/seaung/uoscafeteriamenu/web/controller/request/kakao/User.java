package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Skill Request에서 사용자에 대한 정보를 userRequest.user에 담아서 제공하고 있습니다.
 */
@Data
@NoArgsConstructor
public class User {
    /**
     * 사용자를 식별할 수 있는 key로 최대 70자의 값을 가지고 있습니다.
     * 이 값은 특정한 bot에서 사용자를 식별할 때 사용할 수 있습니다.
     * 동일한 사용자더라도, 봇이 다르면 다른 id가 발급됩니다.
     */
    private String id;
    private String type;
    private Property properties;

    @Builder
    private User(String id, String type, Property properties) {
        this.id = id;
        this.type = type;
        this.properties = properties;
    }
}
