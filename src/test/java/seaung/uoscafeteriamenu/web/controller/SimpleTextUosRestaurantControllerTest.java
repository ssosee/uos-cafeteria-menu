package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.utils.CrawlingDateUtils;
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SimpleTextUosRestaurantControllerTest extends ControllerTestSupport {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("학생회관1층 조식 메뉴를 조회한다.")
    void getUosRestaurantMenu() throws Exception {
        // given
        UosRestaurant uosRestaurant = createUosRestaurant(CrawlingDateUtils.toString(LocalDateTime.now()), UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
        uosRestaurantRepository.save(uosRestaurant);

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/simple-text/uos/restaurant/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text")
                        .value(UosRestaurantName.STUDENT_HALL.getKrName()+"\n"+MealType.BREAKFAST.getKrName()+"\n라면"))
                .andExpect(jsonPath("$.template.outputs[1].simpleImage").doesNotExist())
                .andExpect(jsonPath("$.template.outputs[2].basicCard").doesNotExist())
                .andExpect(jsonPath("$.template.outputs[3].textCard").doesNotExist())
                .andExpect(jsonPath("$.template.quickReplies").isEmpty())
                .andExpect(jsonPath("$.context").isEmpty())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    private SkillPayload createSkillPayload() {
        User user = User.builder()
                .id("botUserKey")
                .type("botUserType")
                .build();

        UserRequest userRequest = UserRequest.builder()
                .timezone("Asia/Seoul")
                .block(Block.builder().id("blockId").name("블록이름").build())
                .utterance("발화 내용")
                .user(user)
                .build();

        Bot bot = Bot.builder()
                .id("botId")
                .name("봇이름")
                .build();

        Intent intent = Intent.builder()
                .id("intentId")
                .name("intentName")
                .build();

        Map<String, String> param = new HashMap<>();
        param.put("restaurantName", "STUDENT_HALL");
        param.put("mealType", "BREAKFAST");

        Action action = Action.builder()
                .id("actionId")
                .params(param)
                .id("actionId")
                .build();

        return SkillPayload.builder()
                .intent(intent)
                .userRequest(userRequest)
                .bot(bot)
                .action(action)
                .build();
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
    }
}