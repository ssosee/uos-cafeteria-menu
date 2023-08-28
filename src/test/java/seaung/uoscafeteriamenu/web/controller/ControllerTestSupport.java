package seaung.uoscafeteriamenu.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.entity.*;
import seaung.uoscafeteriamenu.domain.repository.ApiUserMemberRepository;
import seaung.uoscafeteriamenu.domain.repository.ApikeyRepository;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper om;
    @MockBean
    protected TimeProvider timeProvider;
    @Autowired
    protected ApikeyRepository apikeyRepository;
    @Autowired
    protected ApiUserMemberRepository apiUserMemberRepository;
    @Autowired
    SkillBlockRepository skillBlockRepository;

    @Value("${botApikey}")
    protected String botApikey;
}
