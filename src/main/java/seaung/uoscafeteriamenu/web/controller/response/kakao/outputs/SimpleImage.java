package seaung.uoscafeteriamenu.web.controller.response.kakao.outputs;

import lombok.Data;

@Data
public class SimpleImage extends Outputs {
    private String imageUrl; // 전달하고자 하는 이미지의 url입니다
    private String altText; // url이 유효하지 않은 경우, 전달되는 텍스트입니다
}
