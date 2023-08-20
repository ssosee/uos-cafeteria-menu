package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;
import seaung.uoscafeteriamenu.web.exception.UosRestaurantMenuException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static seaung.uoscafeteriamenu.domain.entity.UosRestaurantName.*;


class TextCardUosRestaurantControllerTest extends ControllerTestSupport {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("학교식당이름과 식사종류로 메뉴를 조회하고 textCard 형식의 응답을 준다.")
    void getUosRestaurantMenu() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        uosRestaurantRepository.save(uosRestaurant);

        SkillPayload skillPayload = createSkillPayload(UosRestaurantName.STUDENT_HALL.name(), MealType.BREAKFAST.name());

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].textCard").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].textCard.text")
                        .value(UosRestaurantName.STUDENT_HALL.getKrName()
                                +"("+MealType.BREAKFAST.getKrName()+")"
                                +"\n👀 조회수: 1"
                                +"\n👍 추천수: 0"
                                +"\n\n라면"))
                .andExpect(jsonPath("$.template.outputs[0].textCard.buttons[0]").isNotEmpty());
    }

    @Test
    @DisplayName("학교식당이름과 식사종류로 메뉴를 조회할 때 진짜 메뉴를 제공받지 못했으면(e.g] 금일 학교사정상 운영안함) simpleText 형식의 응답을 준다.")
    void getUosRestaurantMenuException() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);
        UosRestaurant uosRestaurant = createUosRestaurant(date, UosRestaurantName.STUDENT_HALL, MealType.BREAKFAST, CrawlingUtils.NOT_PROVIDED_MENU, 0, 0);
        uosRestaurantRepository.save(uosRestaurant);

        SkillPayload skillPayload = createSkillPayload(UosRestaurantName.STUDENT_HALL.name(), MealType.BREAKFAST.name());

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu")
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
                                +"\n\n"+CrawlingUtils.NOT_PROVIDED_MENU));
    }

    @Test
    @DisplayName("조회수가 가장 많은 메뉴를 1개 조회한다.(조회수가 같으면 추천수가 많은 순으로 조회)")
    void getTop1UosRestaurantMenuByView() throws Exception {
        // given
        // 현재 시간을 고정할 시간 생성
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);
        UosRestaurant uosRestaurant1 = createUosRestaurant(date, STUDENT_HALL, MealType.BREAKFAST, "라면", 0, 0);
        UosRestaurant uosRestaurant2 = createUosRestaurant(date, MAIN_BUILDING, MealType.BREAKFAST, "김밥", 1, 0);
        UosRestaurant uosRestaurant3 = createUosRestaurant(date, WESTERN_RESTAURANT, MealType.BREAKFAST, "돈까스", 2, 0);
        UosRestaurant uosRestaurant4 = createUosRestaurant(date, MUSEUM_OF_NATURAL_SCIENCE, MealType.BREAKFAST, "제육", 2, 1);
        uosRestaurantRepository.saveAll(List.of(uosRestaurant1, uosRestaurant2, uosRestaurant3, uosRestaurant4));

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu/top1-view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload))
                        .content(om.writeValueAsString(PageRequest.of(0, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].textCard").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].textCard.text")
                        .value(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName()
                        +"("+MealType.BREAKFAST.getKrName()+")"
                        +"\n👀 조회수: 3"
                        +"\n👍 추천수: 1"
                        +"\n\n제육"));
    }

    @Test
    @DisplayName("추천수가 가장 많은 메뉴를 1개 조회한다.(추천수가 같으면 조회수가 많은 순으로 조회)")
    void getTop1UosRestaurantMenuByLikeCount() throws Exception {
        // given
        // 현재 시간을 고정할 시간 생성
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 10, 59, 59);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        String date = CrawlingUtils.toDateString(fixedDateTime);
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
                .andExpect(jsonPath("$.template.outputs[0].textCard").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].textCard.text")
                        .value(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName()
                                +"("+MealType.BREAKFAST.getKrName()+")"
                                +"\n👀 조회수: 4"
                                +"\n👍 추천수: 2"
                                +"\n\n제육"));
    }

    @Test
    @DisplayName("추천메뉴 조회시 운영시간이 아닌 경우 simpleText 형식으로 예외응답을 준다.")
    void getTop1LikeUosRestaurantsMenuCLOSED() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu/top1-like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text").value(UosRestaurantMenuException.CLOSED));
    }

    @Test
    @DisplayName("친기메뉴 조회시 운영시간이 아닌 경우 simpleText 형식으로 예외응답을 준다.")
    void getTop1ViewUosRestaurantsMenuCLOSED() throws Exception {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 8, 16, 18, 30, 0);
        when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        SkillPayload skillPayload = createSkillPayload();

        // when // then
        mockMvc.perform(post("/api/v1/text-card/uos/restaurant/menu/top1-view")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(skillPayload)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.version").value(SkillResponse.apiVersion))
                .andExpect(jsonPath("$.template").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs").isArray())
                .andExpect(jsonPath("$.template.outputs[0].simpleText").isNotEmpty())
                .andExpect(jsonPath("$.template.outputs[0].simpleText.text").value(UosRestaurantMenuException.CLOSED));
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