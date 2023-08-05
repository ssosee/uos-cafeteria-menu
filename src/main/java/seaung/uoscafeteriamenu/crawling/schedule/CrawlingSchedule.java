package seaung.uoscafeteriamenu.crawling.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.CrawlingResponse;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingSchedule {

    private final Crawler crawler;
    private final String cssQuery = "div.listType02#week table tbody tr";

    private final String studentHallUrl = "https://english.uos.ac.kr/food/placeList.do";
    private final String mainBuildingUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=010&menuid=2000005006002000000";
    private final String museumOfNaturalScienceUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=040&menuid=2000005006002000000";
    private final String westernRestaurantUrl = "https://www.uos.ac.kr/food/placeList.do?rstcde=030&menuid=2000005006002000000";

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
    @Scheduled(cron = "0 1 0 * * 1")
    //@Scheduled(cron = "* * * * * *")
    public void crawling() throws IOException {
        List<CrawlingResponse> crawlingResponses = crawler.crawlingFrom(studentHallUrl, cssQuery);
        log.info("크롤링 수행");
        crawlingResponses.forEach(r -> System.out.println(r.getDate()));
    }
}
