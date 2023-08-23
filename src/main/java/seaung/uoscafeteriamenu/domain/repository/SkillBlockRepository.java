package seaung.uoscafeteriamenu.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;

import java.util.List;
import java.util.Optional;

public interface SkillBlockRepository extends JpaRepository<SkillBlock, Long> {
    List<SkillBlock> findByParentBlockNameContains(String parentBlockName);
    Optional<SkillBlock> findByBlockName(BlockName blockName);
}
