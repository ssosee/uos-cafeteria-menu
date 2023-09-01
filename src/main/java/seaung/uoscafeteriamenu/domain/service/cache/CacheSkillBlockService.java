package seaung.uoscafeteriamenu.domain.service.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheSkillBlock;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheSkillBlockService {

    private final SkillBlockRepository skillBlockRepository;

    @Cacheable(value = "cacheSkillBlock", key = "#blockName")
    public CacheSkillBlock getSkillBlockByBlockName(BlockName blockName) {

        SkillBlock skillBlock = skillBlockRepository.findByBlockName(blockName)
                .orElseThrow(() -> new RuntimeException("스킬 블록을 확인하세요."));

        return CacheSkillBlock.of(skillBlock);
    }

    @Cacheable(value = "cacheSkillBlock", key = "#parentBlockName")
    public List<CacheSkillBlock> getSkillBlocksByParentBlockName(String parentBlockName) {

        List<SkillBlock> skillBlocks = skillBlockRepository.findByParentBlockNameContains(parentBlockName);

        return skillBlocks.stream()
                .map(CacheSkillBlock::of)
                .collect(Collectors.toList());
    }
}
