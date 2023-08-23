package seaung.uoscafeteriamenu.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.service.CrawlingUosRestaurantService;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.entity.SkillBlock;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LocalInitData {

    private final CrawlingTargetRepository crawlingTargetRepository;
    private final Crawler crawler;
    private final CrawlingUosRestaurantService crawlingStudentHallService;
    private final SkillBlockRepository skillBlockRepository;

    private final String cssQuery = "div.listType02#week table tbody tr";
    private final String studentHallUrl = "https://www.uos.ac.kr/food/placeList.do?epTicket=INV";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000&epTicket=INV";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000&epTicket=INV";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000&epTicket=INV";
    private final String welfareTeamTel = "02-6490-5855";

    private final Map<UosRestaurantName, String> urlMap = new HashMap<>();
    private final Map<BlockName, String> bockIdMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {

        // 스킬블록 초기화
        skillBlockRepository.saveAll(createSkillBlocks());

        // url 정보 초기화
        initUrlMap();

        // 크롤링 정보 저장
        for(UosRestaurantName name : UosRestaurantName.values()) {
            boolean isCrawlingTarget = crawlingTargetRepository.findByRestaurantsName(name).isPresent();
            if(!isCrawlingTarget) {
                CrawlingTarget target = createTarget(name, urlMap.get(name), welfareTeamTel, cssQuery);
                crawlingTargetRepository.save(target);
            }
        }

        // 크롤링 수행
        List<List<UosRestaurantCrawlingResponse>> responseList = new ArrayList<>();
        for(UosRestaurantName name : UosRestaurantName.values()) {
            List<UosRestaurantCrawlingResponse> responses = crawler.crawlingFrom(name.getKrName(),
                    urlMap.get(name),
                    cssQuery);

            responseList.add(responses);
        }

        // 크롤링 결과 저장
        crawlingStudentHallService.saveAllCrawlingData(responseList);
    }

    private void initUrlMap() {
        urlMap.put(UosRestaurantName.STUDENT_HALL, studentHallUrl);
        urlMap.put(UosRestaurantName.MAIN_BUILDING, mainBuildingUrl);
        urlMap.put(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, museumOfNaturalScienceUrl);
        urlMap.put(UosRestaurantName.WESTERN_RESTAURANT, westernRestaurantUrl);
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

    private CrawlingTarget createTarget(UosRestaurantName uosRestaurantName, String url, String welfareTeamTel, String cssQuery) {
        return CrawlingTarget.builder()
                .restaurantsName(uosRestaurantName)
                .url(url)
                .welfareTeamTel(welfareTeamTel)
                .cssQuery(cssQuery)
                .build();
    }
}
