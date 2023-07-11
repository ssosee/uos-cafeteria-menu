package seaung.uoscafeteriamenu.web.controller.request;

import lombok.Data;

@Data
public class SkillPayload {
    private Intent intent;
    private UserRequest userRequest;
    private Bot bot;
    private Action action;
}
