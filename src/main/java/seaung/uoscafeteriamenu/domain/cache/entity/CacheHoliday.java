package seaung.uoscafeteriamenu.domain.cache.entity;

import static seaung.uoscafeteriamenu.domain.cache.entity.RedisEntityConfig.DEFAULT_TTL;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItem;
@Getter
@EqualsAndHashCode
@RedisHash(value = "cacheHoliday", timeToLive = DEFAULT_TTL)
public class CacheHoliday implements Serializable {
    @Id
    private String locdate;
    private String dateName;
    private String isHoliday;

    @Builder
    private CacheHoliday(String locdate, String dateName, String isHoliday) {
        this.locdate = locdate;
        this.dateName = dateName;
        this.isHoliday = isHoliday;
    }

    public static CacheHoliday of(HolidayItem item) {
        return CacheHoliday.builder()
                .locdate(item.getLocdate())
                .dateName(item.getDateName())
                .isHoliday(item.getIsHoliday())
                .build();
    }
}

