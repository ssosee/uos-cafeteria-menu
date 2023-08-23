package seaung.uoscafeteriamenu.domain.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SkillBlockRepositoryTest {

    @Autowired
    SkillBlockRepository skillBlockRepository;

    @Test
    @DisplayName("block_name을 포함하는 SkillBlock을 조회한다.")
    void findByBlockNameContains() {
        // given
        SkillBlock skillBlock1 = createSkillBlock("1", BlockName.STUDENT_HALL_BREAKFAST, "block", "조식", "냠냠", UosRestaurantName.STUDENT_HALL.name());
        SkillBlock skillBlock2 = createSkillBlock("2", BlockName.STUDENT_HALL_LUNCH, "block", "중식", "냠냠", UosRestaurantName.STUDENT_HALL.name());
        skillBlockRepository.saveAll(List.of(skillBlock1, skillBlock2));

        // when
        List<SkillBlock> result = skillBlockRepository.findByParentBlockNameContains(UosRestaurantName.STUDENT_HALL.name());

        // then
        assertThat(result).hasSize(2)
                .extracting("blockId", "blockName")
                .contains(
                        tuple("1", BlockName.STUDENT_HALL_BREAKFAST),
                        tuple("2", BlockName.STUDENT_HALL_LUNCH)
                );
    }

    private SkillBlock createSkillBlock(String blockId, BlockName blockName, String action, String label, String messageText, String relationalBlockName) {
        SkillBlock skillBlock = SkillBlock.builder()
                .blockId(blockId)
                .blockName(blockName)
                .action(action)
                .label(label)
                .messageText(messageText)
                .relationalBlockName(relationalBlockName)
                .build();

        return skillBlock;
    }
}