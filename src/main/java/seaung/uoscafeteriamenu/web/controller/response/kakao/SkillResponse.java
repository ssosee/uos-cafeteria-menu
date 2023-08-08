package seaung.uoscafeteriamenu.web.controller.response.kakao;

import lombok.Builder;

import java.util.Map;

public class SkillResponse {
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
}
