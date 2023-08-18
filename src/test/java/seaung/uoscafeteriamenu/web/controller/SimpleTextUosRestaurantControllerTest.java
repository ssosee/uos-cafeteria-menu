package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SimpleTextUosRestaurantControllerTest extends ControllerTestSupport {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("학교식당이름과 식사종류로 메뉴를 조회하고 simpleText 형식으로 응답을 준다.")
    void getUosRestaurantMenu() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        uosRestaurantRepository.save(uosRestaurant);

        SkillPayload skillPayload = createSkillPayload(UosRestaurantName.STUDENT_HALL.name(), MealType.BREAKFAST.name());

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
                        .value(UosRestaurantName.STUDENT_HALL.getKrName()
                                +"("+MealType.BREAKFAST.getKrName()+")"
                                +"\n👀 조회수: 1"
                                +"\n👍 추천수: 0"
                                +"\n\n라면"));
    }

    @Test
    @DisplayName("학교식당이름과 식사종류 알맞은 메뉴가 없으면 예외(200)가 발생하고 simpleText 형식으로 응답을 준다.")
    void getUosRestaurantMenuException() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        SkillPayload skillPayload = createSkillPayload(UosRestaurantName.STUDENT_HALL.name(), MealType.BREAKFAST.name());

        // when // then
        mockMvc.perform(post("/api/v1/simple-text/uos/restaurant/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text")
                        .value(UosRestaurantMenuException.NOT_FOUND_MENU))
                .andExpect(jsonPath("$.template.outputs[1].simpleImage").doesNotExist())
                .andExpect(jsonPath("$.template.outputs[2].basicCard").doesNotExist())
                .andExpect(jsonPath("$.template.outputs[3].textCard").doesNotExist())
                .andExpect(jsonPath("$.template.quickReplies").doesNotExist())
                .andExpect(jsonPath("$.context").doesNotExist())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("식사종류로 메뉴들을 조회하고 simpleText 형식으로 응답을 준다.")
    void getUosRestaurantsMenu() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);

        UosRestaurant uosRestaurant1 = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, UosRestaurantName.MAIN_BUILDING, MealType.BREAKFAST, "김밥", 0, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, UosRestaurantName.WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 0, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 0, 0);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        SkillPayload skillPayload = createSkillPayload(MealType.BREAKFAST.name());

        // when // then
        mockMvc.perform(post("/api/v1/simple-text/uos/restaurants/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text").isString());
    }


    private SkillPayload createSkillPayload(String mealType) {
        User user = createUser();

        UserRequest userRequest = createUserRequest(user);

        Bot bot = createBot();

        Intent intent = createIntent();

        Map<String, String> param = new HashMap<>();
        param.put("mealType", mealType);

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

    private SkillPayload createSkillPayload(String restaurantName, String mealType) {
        User user = createUser();

        UserRequest userRequest = createUserRequest(user);

        Bot bot = createBot();

        Intent intent = createIntent();

        Map<String, String> param = new HashMap<>();
        param.put("restaurantName", restaurantName);
        param.put("mealType", mealType);

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

    private UosRestaurant createUosRestaurant(String date, UosRestaurantName uosRestaurantName, MealType mealType, String menu) {
        return UosRestaurant.builder()
                .crawlingDate(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .menuDesc(menu)
                .build();
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