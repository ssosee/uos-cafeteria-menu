package seaung.uoscafeteriamenu.web.controller.response.kakao;

import lombok.Data;

import java.util.Map;

@Data
public class ContextValue {
    private String name; // 수정하려는 output 컨텍스트의 이름
    private int lifeSpan; // 수정하려는 ouptut 컨텍스트의 lifeSpan
    private Map<String, String> params; // output 컨텍스트에 저장하는 추가 데이터
}
