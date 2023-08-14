package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.MealType;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurant;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.UosRestaurantRepository;
import seaung.uoscafeteriamenu.web.controller.request.kakao.*;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TextCardUosRestaurantControllerTest extends ControllerTestSupport {

    @Autowired
    UosRestaurantRepository uosRestaurantRepository;

    @Test
    @DisplayName("학교식당이름과 식사종류로 메뉴를 조회하고 textCard 형식의 응답을 준다.")
    void getUosRestaurantMenu() throws Exception {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
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
                                +"\n👍 좋아요: 0"
                                +"\n\n라면"))
                .andExpect(jsonPath("$.template.outputs[0].textCard.buttons[0]").isNotEmpty());
    }

    @Test
    @DisplayName("학교식당이름과 식사종류로 메뉴를 조회할 때 진짜 메뉴를 제공받지 못했으면(e.g] 금일 학교사정상 운영안함) simpleText 형식의 응답을 준다.")
    void getUosRestaurantMenuException() throws Exception {
        // given
        String date = CrawlingUtils.toDateString(LocalDateTime.now());
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
                                +"\n👍 좋아요: 0"
                                +"\n\n"+CrawlingUtils.NOT_PROVIDED_MENU));
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