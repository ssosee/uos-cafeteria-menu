package seaung.uoscafeteriamenu.api.korea.holiday.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import lombok.Data;

@Data
public class HolidayItems {
    @JsonDeserialize(using = HolidayItemDeserializer.class)
    private List<HolidayItem> item;
}
