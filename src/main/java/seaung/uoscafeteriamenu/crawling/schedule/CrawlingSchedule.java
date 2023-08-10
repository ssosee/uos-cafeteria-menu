package seaung.uoscafeteriamenu.crawling.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.service.CrawlingStudentHallService;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingSchedule {

    private final Crawler crawler;

    private final CrawlingStudentHallService crawlingStudentHallService;

    /**
     * cron = "1 2 3 4 5 6"
     * @1: 초(0-59)
     * @2: 분(0-59)
     * @3: 시(0-23)
     * @4: 일(1-31)
     * @5: 월(1-12)
     * @6: 요일(0-7) 0과 7은 일요일, 1은 월요일 6은 토요일
     */
    // 매주 월요일마다 실행
    // @Scheduled(cron = "0 1 0 * * MON")
    // @Scheduled(cron = "*/30 * * * * *")
    public void crawlingStudentHall() throws IOException {
        // 크롤링 대상 정보 조회
        CrawlingTarget studentHallCrawlingTarget =
                crawlingStudentHallService.findCrawlingTargetBy(UosRestaurantName.STUDENT_HALL);

        CrawlingTarget mainBuildingCrawlingTarget =
                crawlingStudentHallService.findCrawlingTargetBy(UosRestaurantName.MAIN_BUILDING);

        CrawlingTarget museumOfNaturalScienceCrawlingTarget =
                crawlingStudentHallService.findCrawlingTargetBy(UosRestaurantName.STUDENT_HALL);

        CrawlingTarget westernRestaurantCrawlingTarget =
                crawlingStudentHallService.findCrawlingTargetBy(UosRestaurantName.STUDENT_HALL);

        // 학생회관 크롤링
        List<UosRestaurantCrawlingResponse> studentHallCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.STUDENT_HALL.getKrName(),
                studentHallCrawlingTarget.getUrl(),
                studentHallCrawlingTarget.getCssQuery());

        // 본관8층 크롤링
        List<UosRestaurantCrawlingResponse> mainBuildingCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.MAIN_BUILDING.getKrName(),
                mainBuildingCrawlingTarget.getUrl(),
                mainBuildingCrawlingTarget.getCssQuery());

        // 자연과학관 크롤링
        List<UosRestaurantCrawlingResponse> museumOfNaturalScienceCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.MUSEUM_OF_NATURAL_SCIENCE.getKrName(),
                museumOfNaturalScienceCrawlingTarget.getUrl(),
                museumOfNaturalScienceCrawlingTarget.getCssQuery());

        // 양식당 크롤링
        List<UosRestaurantCrawlingResponse> westernRestaurantCrawlingResponse = crawler.crawlingFrom(UosRestaurantName.WESTERN_RESTAURANT.getKrName(),
                westernRestaurantCrawlingTarget.getUrl(),
                westernRestaurantCrawlingTarget.getCssQuery());

        // 크롤링 결과 저장
        List<List<UosRestaurantCrawlingResponse>> crawlingResponseList = List.of(studentHallCrawlingResponse,
                mainBuildingCrawlingResponse,
                museumOfNaturalScienceCrawlingResponse,
                westernRestaurantCrawlingResponse);

        crawlingStudentHallService.saveAllCrawlingData(crawlingResponseList);
    }
}
