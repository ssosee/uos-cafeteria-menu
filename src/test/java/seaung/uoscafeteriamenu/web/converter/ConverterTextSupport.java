package seaung.uoscafeteriamenu.web.converter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConverterTextSupport {

    @Autowired
    private SkillBlockRepository skillBlockRepository;

    @BeforeAll
    void setup() {
        // 스킬블록 초기화
        skillBlockRepository.saveAll(createSkillBlocks());
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
