package seaung.uoscafeteriamenu.web.controller.request.kakao;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Knowledge {
    private String answer; // 지식의 답변
    private String question; // 지식의 질문
    private List<String> categories; // QA의 카테고리
    private String landingUrl; // 지식 탑변에서 링크 더보기
    private String imageUrl; // 지식 답변에서 썸네일 이미지

    @Builder
    private Knowledge(String answer, String question, List<String> categories, String landingUrl, String imageUrl) {
        this.answer = answer;
        this.question = question;
        this.categories = categories;
        this.landingUrl = landingUrl;
        this.imageUrl = imageUrl;
    }
}
