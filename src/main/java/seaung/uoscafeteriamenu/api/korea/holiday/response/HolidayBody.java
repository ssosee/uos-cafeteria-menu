package seaung.uoscafeteriamenu.api.korea.holiday.response;

import lombok.Data;

@Data
public class HolidayBody {
    private HolidayItems items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
