package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Data;

import java.util.List;

@Data
public class Extra {
    private List<Knowledge> matchedKnowledges; // 발화에 일치한 지식 목록입니다.
}
