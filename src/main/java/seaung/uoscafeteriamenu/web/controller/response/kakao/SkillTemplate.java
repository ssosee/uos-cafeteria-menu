package seaung.uoscafeteriamenu.web.controller.response.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Outputs> outputs = new ArrayList<>(Arrays.asList(new Outputs[3])); // 최대 3개 까지 가능
    private List<QuickReply> quickReplies; // 바로가기 그룹
}
