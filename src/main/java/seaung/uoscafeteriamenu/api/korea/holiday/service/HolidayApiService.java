package seaung.uoscafeteriamenu.api.korea.holiday.service;

import static seaung.uoscafeteriamenu.web.exception.HolidayException.NOT_PROVIDE_MENU_AT_HOLIDAY;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import seaung.uoscafeteriamenu.api.korea.holiday.SpecialHoliday;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItems;
import seaung.uoscafeteriamenu.domain.cache.entity.CacheHoliday;
import seaung.uoscafeteriamenu.domain.cache.repository.CacheHolidayRepository;
import seaung.uoscafeteriamenu.web.exception.HolidayException;
import seaung.uoscafeteriamenu.web.exception.SpecialHolidayException;

@Service
@RequiredArgsConstructor
public class HolidayApiService {
    private final static String DATE_PATTERN = "yyyyMMdd";
    private final static String HOLIDAY_TURE = "Y";

    private final CacheHolidayRepository cacheHolidayRepository;

    public void refreshHolidaysInCache(HolidayItems holidayItems) {
        if(!ObjectUtils.isEmpty(holidayItems)) {
            List<CacheHoliday> cacheHolidays = getCacheHolidays(holidayItems);
            cacheHolidayRepository.saveAll(cacheHolidays);
        }
    }

    private List<CacheHoliday> getCacheHolidays(HolidayItems holidayItems) {
        return holidayItems.getItem().stream()
                .filter(Objects::nonNull)
                .map(CacheHoliday::of)
                .collect(Collectors.toList());
    }

    public void checkHolidayInCache(LocalDateTime now) {
        String id = now.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        Optional<CacheHoliday> holidayOptional = cacheHolidayRepository.findById(id);

        if(holidayOptional.isPresent() && holidayOptional.get().getIsHoliday().equals(HOLIDAY_TURE)) {
            String dateName = holidayOptional.get().getDateName();
            checkSpecialHoliday(dateName);

            throw new HolidayException(String.format(NOT_PROVIDE_MENU_AT_HOLIDAY, dateName));
        }
    }

    private void checkSpecialHoliday(String dateName) {
        for(SpecialHoliday specialHoliday : SpecialHoliday.values()) {
            if(specialHoliday.getKrName().contains(dateName)) {
                throw new SpecialHolidayException(specialHoliday.getSpecialMessage());
            }
        }
    }
}
