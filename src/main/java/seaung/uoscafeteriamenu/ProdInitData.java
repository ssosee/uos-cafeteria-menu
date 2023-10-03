package seaung.uoscafeteriamenu;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.service.CrawlingUosRestaurantService;
import seaung.uoscafeteriamenu.domain.entity.BlockName;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;
import seaung.uoscafeteriamenu.domain.repository.SkillBlockRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile("prod")
@RequiredArgsConstructor
public class ProdInitData {

    private final CrawlingTargetRepository crawlingTargetRepository;
    private final Crawler crawler;
    private final CrawlingUosRestaurantService crawlingUosRestaurantService;

    private final String cssQuery = "div.listType02#week table tbody tr";
    private final String studentHallUrl = "https://www.uos.ac.kr/food/placeList.do?epTicket=INV";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000&epTicket=INV";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000&epTicket=INV";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000&epTicket=INV";
    private final String welfareTeamTel = "02-6490-5855";

    private final Map<UosRestaurantName, String> urlMap = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {

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
        crawlingUosRestaurantService.saveAllCrawlingData(responseList);
    }

    private void initUrlMap() {
        urlMap.put(UosRestaurantName.STUDENT_HALL, studentHallUrl);
        urlMap.put(UosRestaurantName.MAIN_BUILDING, mainBuildingUrl);
        urlMap.put(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, museumOfNaturalScienceUrl);
        urlMap.put(UosRestaurantName.WESTERN_RESTAURANT, westernRestaurantUrl);
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


