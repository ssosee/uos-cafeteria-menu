package seaung.uoscafeteriamenu.web.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import seaung.uoscafeteriamenu.domain.entity.*;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;
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

    @Autowired
    SkillBlockRepository skillBlockRepository;

    @BeforeEach
    void setup() {
        // 스킬블록 초기화
        skillBlockRepository.saveAll(createSkillBlocks());

        // apikey 사용 회원 초기화
        ApiUseMember apiUseMember = ApiUseMember.create("master", "howisitgoin@kakao.com");
        apiUserMemberRepository.save(apiUseMember);

        // apikey 저장
        Apikey apikey = Apikey.create(botApikey, apiUseMember);
        apikeyRepository.save(apikey);
    }

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
                        .header("botApikey", botApikey)
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
                        .header("botApikey", botApikey)
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
                        .header("botApikey", botApikey)
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

    private List<SkillBlock> createSkillBlocks() {
        SkillBlock skillBlock1 = createSkillBlock("64ad54f04bc96323949bfb33", BlockName.STUDENT_HALL_BREAKFAST, "block", "\uD83C\uDF73 조식", "냠냠", UosRestaurantName.STUDENT_HALL.name());
        SkillBlock skillBlock2 = createSkillBlock("64d3807a7ad92a7e8643cef2", BlockName.STUDENT_HALL_LUNCH, "block", "\uD83C\uDF5C 중식", "냠냠", UosRestaurantName.STUDENT_HALL.name());
        SkillBlock skillBlock3 = createSkillBlock("64d380a60cdf7a3118c411d5", BlockName.STUDENT_HALL_DINNER, "block", "\uD83C\uDF19 석식", "냠냠", UosRestaurantName.STUDENT_HALL.name());

        SkillBlock skillBlock4 = createSkillBlock("64d38403399c092c9229a63a", BlockName.MUSEUM_OF_NATURAL_BREAKFAST, "block", "\uD83C\uDF73 조식", "냠냠", UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.name());
        SkillBlock skillBlock5 = createSkillBlock("64d384079f905a5d76fabed5", BlockName.MUSEUM_OF_NATURAL_LUNCH, "block", "\uD83C\uDF5C 중식", "냠냠", UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.name());
        SkillBlock skillBlock6 = createSkillBlock("64d3840a7ad92a7e8643cf05", BlockName.MUSEUM_OF_NATURAL_DINNER, "block", "\uD83C\uDF19 석식", "냠냠", UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.name());

        SkillBlock skillBlock7 = createSkillBlock("64d4e9eaf8866579ce31f22e", BlockName.MAIN_BUILDING_BREAKFAST, "block", "\uD83C\uDF73 조식", "냠냠", UosRestaurantName.MAIN_BUILDING.name());
        SkillBlock skillBlock8 = createSkillBlock("64d4e9edb3711745ae28cb4e", BlockName.MAIN_BUILDING_LUNCH, "block", "\uD83C\uDF5C 중식", "냠냠", UosRestaurantName.MAIN_BUILDING.name());
        SkillBlock skillBlock9 = createSkillBlock("64d4e9f0bc2fe742fffde458", BlockName.MAIN_BUILDING_DINNER, "block", "\uD83C\uDF19 석식", "냠냠", UosRestaurantName.MAIN_BUILDING.name());

        SkillBlock skillBlock10 = createSkillBlock("64d4e9faf8866579ce31f230", BlockName.WESTERN_RESTAURANT_BREAKFAST, "block", "\uD83C\uDF73 조식", "냠냠", UosRestaurantName.WESTERN_RESTAURANT.name());
        SkillBlock skillBlock11 = createSkillBlock("64d4e9ffb3711745ae28cb50", BlockName.WESTERN_RESTAURANT_LUNCH, "block", "\uD83C\uDF5C 중식", "냠냠", UosRestaurantName.WESTERN_RESTAURANT.name());
        SkillBlock skillBlock12 = createSkillBlock("64d4ea04bc2fe742fffde45a", BlockName.WESTERN_RESTAURANT_DINNER, "block", "\uD83C\uDF19 석식", "냠냠", UosRestaurantName.WESTERN_RESTAURANT.name());

        SkillBlock skillBlock13 = createSkillBlock("64d75083c800862a54172c4a", BlockName.MENU_RECOMMEND, "block", "\uD83D\uDE0B 추천", "강력 추천", "RESTAURANT");

        return List.of(skillBlock1, skillBlock2,
                skillBlock3, skillBlock4,
                skillBlock5, skillBlock6,
                skillBlock7, skillBlock8,
                skillBlock9, skillBlock10,
                skillBlock11, skillBlock12,
                skillBlock13);
    }

    private SkillBlock createSkillBlock(String blockId, BlockName blockName, String action, String label, String messageText, String parentBlockName) {
        SkillBlock skillBlock = SkillBlock.builder()
                .blockId(blockId)
                .blockName(blockName)
                .action(action)
                .label(label)
                .messageText(messageText)
                .parentBlockName(parentBlockName)
                .build();

        return skillBlock;
    }
}