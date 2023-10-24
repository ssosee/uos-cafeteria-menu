package seaung.uoscafeteriamenu.global.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheUosRestaurantRepository;
import seaung.uoscafeteriamenu.domain.service.UosRestaurantService;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UosRestaurantSchedule {
    private final UosRestaurantService uosRestaurantService;
    private final CacheUosRestaurantRepository cacheUosRestaurantRepository;
    private final TimeProvider timeProvider;

    //@Scheduled(cron = "0/10 * * * * ?")
    @Scheduled(cron = "0 0/10 * * * ?")
    @Async("uosRestaurantAsyncExecutor")
    public void syncCacheMemberVisitCountToDatabaseMemberSchedule() {
        log.info("학식 동기화 시작");

        LocalDateTime now = timeProvider.getCurrentLocalDateTime();
        uosRestaurantService.syncCacheUosRestaurantToDatabaseUosRestaurant(now);

        log.info("학식 동기화 종료");
    }

    // 일, 월, 화, 수, 목요일에 실행
    @Scheduled(cron = "0 0 19 * * SUN,MON,TUE,WED,THU")
    @Async("uosRestaurantAsyncExecutor")
    public void deleteAllCacheUosRestaurantAndWarmUpNextDayUosRestaurant() {
        log.info("금일 학식메뉴 캐시 삭제 / 내일 학식메뉴 warm-up 시작");

        LocalDateTime now = timeProvider.getCurrentLocalDateTime();
        uosRestaurantService.deleteAllCacheUosRestaurantAndWarmUpNextDayUosRestaurant(now);

        log.info("금일 학식메뉴 캐시 삭제 / 내일 학식메뉴 warm-up 종료");
    }
}
