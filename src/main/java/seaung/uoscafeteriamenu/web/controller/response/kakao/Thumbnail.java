package seaung.uoscafeteriamenu.web.controller.response.kakao;

import lombok.Data;

@Data
public class Thumbnail {
    private String imageUrl;
    private Link link;
    private boolean fixedRatio;
}
