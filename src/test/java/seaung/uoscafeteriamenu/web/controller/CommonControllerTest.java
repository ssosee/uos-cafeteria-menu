package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.*;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE;

public class CommonControllerTest extends ControllerTestSupport {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("토요일에는 학식을 제공하지 않는 안내문구를 simpleText로 응답한다.")
    void isSaturday() throws Exception {
        // given
        // 현재 시간을 고정할 시간 생성(토요일)
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 19, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        // 금요일
        LocalDateTime friday = LocalDateTime.of(2023, 8, 18, 10, 59, 59);
        String date = CrawlingUtils.toDateString(friday);
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu/top1-like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload))
                        .content(om.writeValueAsString(PageRequest.of(0, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text")
                        .value(UosRestaurantMenuException.NOT_PROVIDE_MENU_AT_WEEKEND));
    }

    @Test
    @DisplayName("일요일에는 학식을 제공하지 않는 안내문구를 simpleText로 응답한다.")
    void isSunday() throws Exception {
        // given
        // 현재 시간을 고정할 시간 생성(토요일)
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 20, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        // 금요일
        LocalDateTime friday = LocalDateTime.of(2023, 8, 18, 10, 59, 59);
        String date = CrawlingUtils.toDateString(friday);
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 1);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 2);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 3, 2);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu/top1-like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload))
                        .content(om.writeValueAsString(PageRequest.of(0, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text")
                        .value(UosRestaurantMenuException.NOT_PROVIDE_MENU_AT_WEEKEND));
    }

    private SkillPayload createSkillPayload() {
        User user = createUser();

        UserRequest userRequest = createUserRequest(user);

        Bot bot = createBot();

        Intent intent = createIntent();

        Map<String, String> param = new HashMap<>();

        Action action = Action.builder()
                .id("actionId")
                .params(param)
                .build();

        return SkillPayload.builder()
                .intent(intent)
                .userRequest(userRequest)
                .bot(bot)
                .action(action)
                .build();
    }

    private static Intent createIntent() {
        Intent intent = Intent.builder()
                .id("intentId")
                .name("intentName")
                .build();
        return intent;
    }

    private static Bot createBot() {
        Bot bot = Bot.builder()
                .id("botId")
                .name("봇이름")
                .build();
        return bot;
    }

    private static UserRequest createUserRequest(User user) {
        UserRequest userRequest = UserRequest.builder()
                .timezone("Asia/Seoul")
                .block(Block.builder().id("blockId").name("블록이름").build())
                .utterance("발화 내용")
                .user(user)
                .build();
        return userRequest;
    }

    private static User createUser() {
        User user = User.builder()
                .id("botUserKey")
                .type("botUserType")
                .build();
        return user;
    }

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType,
                                              String menu, Integer view, Integer likeCount) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .view(view)
                .likeCount(likeCount)
                .build();
    }
}
