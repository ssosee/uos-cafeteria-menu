package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;

@Data
public class Block {
    private String id;
    private String name;

    @Builder
    private Block(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
