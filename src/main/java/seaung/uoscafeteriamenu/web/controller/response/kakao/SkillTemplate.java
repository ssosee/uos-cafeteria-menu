package seaung.uoscafeteriamenu.web.controller.response.kakao;

import lombok.Data;

import java.util.List;

@Data
public class SkillTemplate<T> {
    /**
     * [simpleText(간단 텍스트),
     *  simpleImage(간단 이미지),
     *  basicCard(기본 카드),
     *  commerceCard(커머스 카드),
     *  listCard(리스트 카드)]
     */
    private List<T> outputs;
    private List<String> quickReplies; // 바로가기 그룹
}
