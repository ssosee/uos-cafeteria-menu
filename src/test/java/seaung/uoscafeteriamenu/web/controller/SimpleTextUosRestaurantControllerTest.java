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
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;

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
//        UosRestaurant uosRestaurant = createUosRestaurant(UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면");
//        uosRestaurantRepository.save(uosRestaurant);
//
//        SkillPayload skillPayload = createSkillPayload();
//
//        // when // then
//        mockMvc.perform(post("/api/v1/simple-text/uos/restaurant/menu")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(om.writeValueAsBytes(skillPayload)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$."))
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

    private UosRestaurant createUosRestaurant(UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
    }
}