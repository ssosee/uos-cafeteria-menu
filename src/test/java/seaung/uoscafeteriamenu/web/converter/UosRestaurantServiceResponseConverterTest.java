package seaung.uoscafeteriamenu.web.converter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import seaung.uoscafeteriamenu.crawling.utils.CrawlingUtils;
import seaung.uoscafeteriamenu.domain.entity.*;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.domain.service.request.UosRestaurantInput;
import seaung.uoscafeteriamenu.domain.service.response.UosRestaurantMenuResponse;
import seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static seaung.uoscafeteriamenu.web.controller.response.kakao.SkillResponse.apiVersion;

@SpringBootTest
class UosRestaurantServiceResponseConverterTest {

    @Autowired
    UosRestaurantServiceResponseConverter uosRestaurantServiceResponseConverter;

    @Autowired
    SkillBlockRepository skillBlockRepository;

    @MockBean
    UosRestaurantService uosRestaurantService;

    @Test
    @DisplayName("학식 메뉴 조회 결과를 TextCard에 추천버튼을 포함하고 QuickReplies를 포함하는 응답으로 변경한다.")
    void getUosRestaurantMenuToSkillResponseUseTextCardWithButtonAndQuickReplies() {
        // given
        skillBlockRepository.saveAll(createSkillBlocks());

        LocalDateTime now = LocalDateTime.of(2023, 8, 24, 10, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant = createUosRestaurant(
                UosRestaurantName.STUDENT_HALL, date, MealType.BREAKFAST, "라면", 0, 0);

        UosRestaurantMenuResponse response = UosRestaurantMenuResponse.of(uosRestaurant);

        // when
        SkillResponse skillResponse = uosRestaurantServiceResponseConverter
                .toSkillResponseUseTextCardWithButtonAndQuickReplies("2.0", response);

        // then
        assertThat(skillResponse).isNotNull();
        assertThat(skillResponse.getTemplate()).isNotNull();
        assertAll(
                () -> assertThat(skillResponse.getVersion()).isEqualTo("2.0"),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard()).isNotNull(),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard().getText()).contains("라면"),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard().getButtons().get(0)).isNotNull()
        );
    }

    @Test
    @DisplayName("학식 메뉴 조회 결과를 TextCard에 추천버튼을 포함하고 QuickReplies를 포함하는 응답으로 변경할 때 추천 스킬블록이 없으면 예외가 발생한다.")
    void getUosRestaurantMenuToSkillResponseUseTextCardWithButtonAndQuickRepliesException() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 8, 24, 10, 0, 0);
        String date = CrawlingUtils.toDateString(now);

        UosRestaurant uosRestaurant = createUosRestaurant(
                UosRestaurantName.STUDENT_HALL, date, MealType.BREAKFAST, "라면", 0, 0);

        UosRestaurantMenuResponse response = UosRestaurantMenuResponse.of(uosRestaurant);

        // when // then
        assertThatThrownBy(() -> uosRestaurantServiceResponseConverter
                .toSkillResponseUseTextCardWithButtonAndQuickReplies(apiVersion, response))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("스킬 블록을 확인하세요.");
    }

    @Disabled
    @Test
    @DisplayName("")
    void test() {
        // given

        // when
        SkillResponse skillResponse = uosRestaurantServiceResponseConverter.toSkillResponseUseSimpleText(apiVersion, "추천");

        // then
        assertThat(skillResponse).isNotNull();
        assertThat(skillResponse.getTemplate()).isNotNull();
        assertAll(
                () -> assertThat(skillResponse.getVersion()).isEqualTo("2.0"),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard()).isNotNull(),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard().getText()).contains("라면"),
                () -> assertThat(skillResponse.getTemplate().getOutputs().get(0).getTextCard().getButtons().get(0)).isNotNull()
        );
    }

    private UosRestaurantInput createUosRestaurantInput(String date, UosRestaurantName uosRestaurantName, MealType mealType) {
        return UosRestaurantInput.builder()
                .date(date)
                .restaurantName(uosRestaurantName)
                .mealType(mealType)
                .build();
    }

    private UosRestaurant createUosRestaurant(UosRestaurantName restaurantName, String crawlingDate, MealType mealType, String menuDesc, Integer view, Integer likeCount) {
        return UosRestaurant.builder()
                .restaurantName(restaurantName)
                .crawlingDate(crawlingDate)
                .mealType(mealType)
                .menuDesc(menuDesc)
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