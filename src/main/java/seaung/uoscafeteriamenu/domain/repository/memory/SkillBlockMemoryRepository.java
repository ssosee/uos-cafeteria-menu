package seaung.uoscafeteriamenu.domain.repository.memory;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SkillBlockMemoryRepository {
    private Map<BlockName, String> blockIdMap = new HashMap<>();
    private Map<UosRestaurantName, List<BlockName>> blockNameMap = new HashMap<>();

    @PostConstruct
    private void init() {
        initBlockIdMap();
        initBlockNameMap();
    }

    private void initBlockNameMap() {
        blockNameMap.put(UosRestaurantName.STUDENT_HALL, List.of(BlockName.STUDENT_HALL_BREAKFAST,
                BlockName.STUDENT_HALL_LUNCH,
                BlockName.STUDENT_HALL_DINNER));

        blockNameMap.put(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, List.of(BlockName.MAIN_BUILDING_BREAKFAST,
                BlockName.MAIN_BUILDING_LUNCH,
                BlockName.MAIN_BUILDING_DINNER));

        blockNameMap.put(UosRestaurantName.MAIN_BUILDING, List.of(BlockName.MAIN_BUILDING_BREAKFAST,
                BlockName.MAIN_BUILDING_LUNCH,
                BlockName.MAIN_BUILDING_DINNER));

        blockNameMap.put(UosRestaurantName.WESTERN_RESTAURANT, List.of(BlockName.WESTERN_RESTAURANT_BREAKFAST,
                BlockName.WESTERN_RESTAURANT_LUNCH,
                BlockName.WESTERN_RESTAURANT_DINNER));
    }

    private void initBlockIdMap() {
        blockIdMap.put(BlockName.STUDENT_HALL_BREAKFAST, "64ad54f04bc96323949bfb33");
        blockIdMap.put(BlockName.STUDENT_HALL_LUNCH, "64d3807a7ad92a7e8643cef2");
        blockIdMap.put(BlockName.STUDENT_HALL_DINNER, "64d380a60cdf7a3118c411d5");

        blockIdMap.put(BlockName.MUSEUM_OF_NATURAL_BREAKFAST, "64d38403399c092c9229a63a");
        blockIdMap.put(BlockName.MUSEUM_OF_NATURAL_LUNCH, "64d384079f905a5d76fabed5");
        blockIdMap.put(BlockName.MUSEUM_OF_NATURAL_DINNER, "64d3840a7ad92a7e8643cf05");

        blockIdMap.put(BlockName.MAIN_BUILDING_BREAKFAST, "64d4e9eaf8866579ce31f22e");
        blockIdMap.put(BlockName.MAIN_BUILDING_LUNCH, "64d4e9edb3711745ae28cb4e");
        blockIdMap.put(BlockName.MAIN_BUILDING_DINNER, "64d4e9f0bc2fe742fffde458");

        blockIdMap.put(BlockName.WESTERN_RESTAURANT_BREAKFAST, "64d4e9faf8866579ce31f230");
        blockIdMap.put(BlockName.WESTERN_RESTAURANT_LUNCH, "64d4e9ffb3711745ae28cb50");
        blockIdMap.put(BlockName.WESTERN_RESTAURANT_DINNER, "64d4ea04bc2fe742fffde45a");
    }

    public String findBlockIdByBlockName(BlockName blockName) {
        return blockIdMap.get(blockName);
    }

    public List<BlockName> findAllBlockNameByUosRestaurantName(UosRestaurantName uosRestaurantName) {
        return blockNameMap.get(uosRestaurantName);
    }

    public Map<BlockName, String> getBlockIdMap() {
        return blockIdMap;
    }

    public Map<UosRestaurantName, List<BlockName>> getBlockNameMap() {
        return blockNameMap;
    }
}
