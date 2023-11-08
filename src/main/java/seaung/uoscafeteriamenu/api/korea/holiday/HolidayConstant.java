package seaung.uoscafeteriamenu.api.korea.holiday;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HolidayConstant {
    public static final String API_BASIC_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService";
    public static final String HOLIDAY_URL_PATH = "getHoliDeInfo";
    public static final String YEAR_KEY = "solYear";
    public static final String MONTH_KEY = "solMonth";
    public static final String TYPE_KEY = "_type";
    public static final String SERVICE_KEY_KEY = "ServiceKey";
    public static final String RESPONSE_TYPE_JSON = "json";

    private HolidayConstant() {
    }
}
