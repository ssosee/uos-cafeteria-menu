package seaung.uoscafeteriamenu.api.korea.holiday.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HolidayResponse {
    private HolidayHeader header;
    private HolidayBody body;
}
