package seaung.uoscafeteriamenu.global.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seaung.uoscafeteriamenu.api.korea.holiday.handler.HolidayApiHandler;
import seaung.uoscafeteriamenu.api.korea.holiday.response.ApiResponse;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayResponse;
import seaung.uoscafeteriamenu.api.korea.holiday.service.HolidayApiService;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidaySchedule {

    private final HolidayApiHandler holidayApiHandler;
    private final HolidayApiService holidayApiService;
    private final TimeProvider timeProvider;

    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    @Async("uosRestaurantAsyncExecutor")
    public void refreshHolidayInCache() {
        log.info("공휴일 정보 캐시 저장 스케줄링 시작");

        ApiResponse<HolidayResponse> response = holidayApiHandler.callHolidaysApi(timeProvider.getCurrentLocalDateTime());
        holidayApiService.refreshHolidaysInCache(response.getResponse().getBody().getItems());

        log.info("공휴일 정보 캐시 저장 스케줄링 종료");
    }
}
