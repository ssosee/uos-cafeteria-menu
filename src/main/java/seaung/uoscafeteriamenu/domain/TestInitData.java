package seaung.uoscafeteriamenu.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.service.CrawlingUosRestaurantService;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;
import seaung.uoscafeteriamenu.domain.repository.CrawlingTargetRepository;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TestInitData {
    private final CrawlingTargetRepository crawlingTargetRepository;
    private final Crawler crawler;
    private final CrawlingUosRestaurantService crawlingStudentHallService;

    private final String uosRestaurantCssQuery = "div.listType02#week table tbody tr";

    private final String studentHallUrl = "https://www.uos.ac.kr/food/placeList.do?epTicket=INV";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000&epTicket=INV";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000&epTicket=INV";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000&epTicket=INV";
    private final String welfareTeamTel = "02-6490-5855";

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        // 크롤링 대상 정보 저장
        CrawlingTarget studentHall = createTarget(UosRestaurantName.STUDENT_HALL, studentHallUrl, welfareTeamTel, uosRestaurantCssQuery);
        CrawlingTarget mainBuilding = createTarget(UosRestaurantName.MAIN_BUILDING, mainBuildingUrl, welfareTeamTel, uosRestaurantCssQuery);
        CrawlingTarget museumOfNaturalScience = createTarget(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE, museumOfNaturalScienceUrl, welfareTeamTel, uosRestaurantCssQuery);
        CrawlingTarget westernRestaurant = createTarget(UosRestaurantName.WESTERN_RESTAURANT, westernRestaurantUrl, welfareTeamTel, uosRestaurantCssQuery);
        crawlingTargetRepository.saveAll(List.of(studentHall, mainBuilding, museumOfNaturalScience, westernRestaurant));

        // 크롤링
        List<UosRestaurantCrawlingResponse> studentHallCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.STUDENT_HALL.getKrName(),
                studentHallUrl,
                uosRestaurantCssQuery);

        List<UosRestaurantCrawlingResponse> mainBuildingCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.MAIN_BUILDING.getKrName(),
                mainBuildingUrl,
                uosRestaurantCssQuery);

        List<UosRestaurantCrawlingResponse> museumOfNaturalScienceCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(),
                museumOfNaturalScienceUrl,
                uosRestaurantCssQuery);

        List<UosRestaurantCrawlingResponse> westernRestaurantCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.WESTERN_RESTAURANT.getKrName(),
                westernRestaurantUrl,
                uosRestaurantCssQuery);

        // 크롤링 결과 저장
        crawlingStudentHallService.saveAllCrawlingData(List.of(studentHallCrawlingResponse, mainBuildingCrawlingResponse, museumOfNaturalScienceCrawlingResponse, westernRestaurantCrawlingResponse));
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
