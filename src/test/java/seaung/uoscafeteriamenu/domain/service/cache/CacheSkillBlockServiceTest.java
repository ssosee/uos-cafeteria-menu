package seaung.uoscafeteriamenu.domain.service.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.transaction.annotation.Transactional;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheSkillBlock;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CacheSkillBlockServiceTest {

    @Autowired
    SkillBlockRepository skillBlockRepository;
    @Autowired
    CacheSkillBlockService cacheSkillBlockService;
    @Autowired
    CacheManager cacheManager;

    @Test
    @DisplayName("캐시에 SkillBlock이 없으면, 캐시에 저장된다.")
    void getSkillBlockByBlockName() {
        // given
        SkillBlock skillBlock = createSkillBlock("1", BlockName.STUDENT_HALL_BREAKFAST, "block", "\uD83C\uDF73 조식", "냠냠", UosRestaurantName.STUDENT_HALL.name());
        skillBlockRepository.save(skillBlock);

        // when
        cacheSkillBlockService.getSkillBlockByBlockName(BlockName.STUDENT_HALL_BREAKFAST);

        // then
        // 메소드 호출을 캐시에 저장하고 캐시에 데이터가 있는지 확인
        CacheSkillBlock cacheSkillBlock = cacheManager.getCache("cacheSkillBlock").get(BlockName.STUDENT_HALL_BREAKFAST, CacheSkillBlock.class);
        assertThat(cacheSkillBlock).isNotNull();

        CacheSkillBlock cachedSkillBlock = cacheSkillBlockService.getSkillBlockByBlockName(BlockName.STUDENT_HALL_BREAKFAST);
        assertEquals(cacheSkillBlock, cachedSkillBlock);
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