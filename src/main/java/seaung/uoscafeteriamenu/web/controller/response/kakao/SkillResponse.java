package seaung.uoscafeteriamenu.web.controller.response.kakao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.Outputs;
import seaung.uoscafeteriamenu.web.controller.response.kakao.outputs.OutputsDto;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkillResponse {
    public static String apiVersion = "2.0";
    private String version;
    private SkillTemplate template;
    private ContextControl context;
    private Map<String, Object> data;

    @Builder
    private SkillResponse(String version, SkillTemplate template, ContextControl context, Map<String, Object> data) {
        this.version = version;
        this.template = template;
        this.context = context;
        this.data = data;
    }

    public static SkillResponse ofSimpleText(String version, String text) {

        Outputs outputs = Outputs.createOutputsUseSimpleText(text);

        SkillTemplate template = new SkillTemplate();
        template.setOutputs(List.of(outputs));

        return SkillResponse.builder()
                .version(version)
                .template(template)
                .build();
    }
}
