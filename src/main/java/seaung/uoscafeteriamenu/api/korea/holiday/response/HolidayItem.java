package seaung.uoscafeteriamenu.api.korea.holiday.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HolidayItem {
    private String dateKind; // 종류
    private String dateName; // 명칭
    private String isHoliday; // 공공기관 휴일여부
    private String locdate; // 날짜
    private int seq; // 순번

    @Builder
    public HolidayItem(String dateKind, String dateName, String isHoliday, String locdate, int seq) {
        this.dateKind = dateKind;
        this.dateName = dateName;
        this.isHoliday = isHoliday;
        this.locdate = locdate;
        this.seq = seq;
    }
}

