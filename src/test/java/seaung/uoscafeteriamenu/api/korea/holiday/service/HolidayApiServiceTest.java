package seaung.uoscafeteriamenu.api.korea.holiday.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItem;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItems;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheHoliday;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheHolidayRepository;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;
import seaung.uoscafeteriamenu.web.exception.HolidayException;
import seaung.uoscafeteriamenu.web.exception.SpecialHolidayException;

@SpringBootTest
class HolidayApiServiceTest {

    @Autowired
    HolidayApiService holidayApiService;

    @MockBean
    TimeProvider timeProvider;

    @Autowired
    CacheHolidayRepository cacheHolidayRepository;

    @AfterEach
    void tearDown() {
        cacheHolidayRepository.deleteAll();
    }

    @Test
    @DisplayName("캐시에 모든 공휴일 정보를 저장한다.")
    void saveHolidaysInCache() {
        // given
        HolidayItem holidayItem1 = create("01", "임시공휴일", "Y", "20231002", 2);
        HolidayItem holidayItem2 = create("01", "개천절", "Y", "20231003", 1);
        HolidayItem holidayItem3 = create("01", "한글날", "Y", "20231009", 1);

        HolidayItems holidayItems = new HolidayItems();
        holidayItems.setItem(List.of(holidayItem1, holidayItem2, holidayItem3));

        // when
        holidayApiService.refreshHolidaysInCache(holidayItems);

        // then
        Iterable<CacheHoliday> holidays = cacheHolidayRepository.findAll();
        List<CacheHoliday> convertHolidays = StreamSupport.stream(holidays.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(convertHolidays).hasSize(3)
                .extracting("locdate", "dateName", "isHoliday")
                .contains(
                        tuple("20231002", "임시공휴일", "Y"),
                        tuple("20231003", "개천절", "Y"),
                        tuple("20231009", "한글날", "Y")
                );
    }

    @Test
    @DisplayName("공휴일 정보가 없으면 저장하지 않는다.")
    void doNotSaveHolidaysInCache() {
        // given
        HolidayItems holidayItems = null;

        // when
        holidayApiService.refreshHolidaysInCache(holidayItems);

        // then
        Iterable<CacheHoliday> holidays = cacheHolidayRepository.findAll();
        List<CacheHoliday> convertHolidays = StreamSupport.stream(holidays.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(convertHolidays).hasSize(0);
    }

    @Test
    @DisplayName("캐시에 저장된 공휴일 정보가 있고, 오늘이 공휴일이라면 예외가 발생한다.")
    void checkHoliday() {
        // given
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, 5, 5, 0,0,0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedLocalDateTime);

        HolidayItem holidayItem = create("01", "어린이날", "Y", "20230505", 1);
        CacheHoliday cacheHoliday = CacheHoliday.of(holidayItem);
        cacheHolidayRepository.save(cacheHoliday);

        // when // then
        assertThatThrownBy(() -> holidayApiService.checkHolidayInCache(timeProvider.getCurrentLocalDateTime()))
                .isInstanceOf(HolidayException.class)
                .hasMessage(String.format(HolidayException.NOT_PROVIDE_MENU_AT_HOLIDAY, "어린이날"));
    }

    @Test
    @DisplayName("캐시에 저장된 국경일 정보가, 오늘이 국경일이라면 예외가 발생하지 않는다.")
    void checkHoliday국경일() {
        // given
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, 7, 17, 0,0,0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedLocalDateTime);

        HolidayItem holidayItem = create("01", "제헌절", "N", "20230717", 1);
        CacheHoliday cacheHoliday = CacheHoliday.of(holidayItem);
        cacheHolidayRepository.save(cacheHoliday);

        // when // then
        holidayApiService.checkHolidayInCache(timeProvider.getCurrentLocalDateTime());
    }

    @Test
    @DisplayName("캐시에 저장된 공휴일 정보가 없으면 예외가 발생하지 않는다.")
    void checkHolidayInCache평일() {
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, 11, 8, 0,0,0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedLocalDateTime);

        // when // then
        holidayApiService.checkHolidayInCache(timeProvider.getCurrentLocalDateTime());
    }

    @ParameterizedTest
    @CsvSource(value = {"1월1일:2023:1:1:20230101",
            "설날:2023:1:22:20230122",
            "추석:2023:9:29:20230929",
            "기독탄신일:2023:12:25:20231225"}, delimiter = ':')
    @DisplayName("1월1일, 설날, 추석, 기독탄신일에는 특별한 메시지를 포함한 예외가 발생한다.")
    void checkSpecialHolidayInCache(String dateName, int year, int month, int day, String locdate) {
        // given
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(year, month, day, 0,0,0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedLocalDateTime);

        HolidayItem holidayItem = create(dateName, "Y", locdate);
        CacheHoliday cacheHoliday = CacheHoliday.of(holidayItem);
        cacheHolidayRepository.save(cacheHoliday);

        // when // then
        assertThatThrownBy(() -> holidayApiService.checkHolidayInCache(timeProvider.getCurrentLocalDateTime()))
                .isInstanceOf(SpecialHolidayException.class);
    }

    private HolidayItem create(String dateName, String isHoliday, String locdate) {
        return HolidayItem.builder()
                .dateName(dateName)
                .isHoliday(isHoliday)
                .locdate(locdate)
                .build();
    }

    private HolidayItem create(String dateKind, String dateName, String isHoliday, String locdate, int seq) {
        return HolidayItem.builder()
                .dateKind(dateKind)
                .dateName(dateName)
                .isHoliday(isHoliday)
                .locdate(locdate)
                .seq(seq)
                .build();
    }
}