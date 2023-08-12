package seaung.uoscafeteriamenu.web.controller.response.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillTemplate {
    /**
     * [simpleText(간단 텍스트),
     *  simpleImage(간단 이미지),
     *  basicCard(기본 카드),
     *  commerceCard(커머스 카드),
     *  listCard(리스트 카드)]
     */
    private List<Outputs> outputs;
    private List<String> quickReplies; // 바로가기 그룹
}
