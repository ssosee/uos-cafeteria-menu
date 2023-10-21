package seaung.uoscafeteriamenu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestDocsController {

    @GetMapping("/test-docs")
    public String testDocs() {
        return "TestResults";
    }
}
