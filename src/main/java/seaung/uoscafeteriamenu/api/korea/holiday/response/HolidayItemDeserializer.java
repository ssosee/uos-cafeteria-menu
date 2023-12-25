package seaung.uoscafeteriamenu.api.korea.holiday.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HolidayItemDeserializer extends JsonDeserializer<List<HolidayItem>> {
    @Override
    public List<HolidayItem> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = jsonParser.readValueAsTree();

        if(jsonNode.isArray()) {
            return getHolidayItemsToArray(jsonNode);
        }

        return getHolidayItemsToObject(jsonNode);
    }

    private List<HolidayItem> getHolidayItemsToObject(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<HolidayItem> holidayItems = new ArrayList<>();
        HolidayItem holidayItem = om.treeToValue(jsonNode, HolidayItem.class);
        holidayItems.add(holidayItem);

        return holidayItems;
    }

    private List<HolidayItem> getHolidayItemsToArray(JsonNode jsonNode) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        List<HolidayItem> holidayItems = new ArrayList<>();
        for(JsonNode item : jsonNode) {
            HolidayItem holidayItem = om.treeToValue(item, HolidayItem.class);
            holidayItems.add(holidayItem);
        }

        return holidayItems;
    }
}
