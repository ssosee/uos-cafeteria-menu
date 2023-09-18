package seaung.uoscafeteriamenu.crawling.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.entity.UosRestaurantName;
import seaung.uoscafeteriamenu.crawling.crawler.Crawler;
import seaung.uoscafeteriamenu.crawling.crawler.UosRestaurantCrawlingResponse;
import seaung.uoscafeteriamenu.crawling.service.CrawlingUosRestaurantService;
import seaung.uoscafeteriamenu.domain.entity.CrawlingTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingSchedule {

    private final Crawler crawler;

    private final CrawlingUosRestaurantService crawlingStudentHallService;

    /**
     * cron = "1 2 3 4 5 6"
     * @1: 초(0-59)
     * @2: 분(0-59)
     * @3: 시(0-23)
     * @4: 일(1-31)
     * @5: 월(1-12)
     * @6: 요일(0-7) 0과 7은 일요일, 1은 월요일 6은 토요일
     */
    //@Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 7 * * MON", zone = "Asia/Seoul") // 매주 월요일 7시에 실행
    @Async("crawlingAsyncExecutor")
    public void crawlingStudentHall() throws IOException {
        log.info("크롤링 시작...");

        List<List<UosRestaurantCrawlingResponse>> crawlingResponseList = new ArrayList<>();
        for(UosRestaurantName restaurantName : UosRestaurantName.values()) {
            // 크롤링에 필요한 타겟 정보 조회
            CrawlingTarget target = crawlingStudentHallService.findCrawlingTargetBy(restaurantName);

            // 크롤링 타겟 크롤링
            List<UosRestaurantCrawlingResponse> responses = crawler.crawlingFrom(restaurantName.getKrName(),
                    target.getUrl(), target.getCssQuery());

            crawlingResponseList.add(responses);
        }

        // 크롤링 데이터 저장
        crawlingStudentHallService.saveAllCrawlingData(crawlingResponseList);

        log.info("크롤링 종료...");
    }
}
