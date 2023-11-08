package seaung.uoscafeteriamenu.api.korea.holiday.handler;

import static seaung.uoscafeteriamenu.api.korea.holiday.HolidayConstant.*;

import java.net.URI;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import seaung.uoscafeteriamenu.api.korea.holiday.HolidayConstant;
import seaung.uoscafeteriamenu.api.korea.holiday.response.ApiResponse;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayApiHandler {

    @Value("${korea.holiday.serviceKey}")
    private String SERVICE_KEY_VALUE;
    private static final String MONTH_FORMAT = "%02d";

    private final RestTemplate restTemplate;
    private final UriComponentsBuilder uriComponentsBuilder;

    public ApiResponse<HolidayResponse> callHolidaysApi(LocalDateTime now) {
        System.out.println(getHolidaysUri(now));
        return restTemplate.exchange(
                getHolidaysUri(now),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ApiResponse<HolidayResponse>>() {

                }).getBody();
    }

    private URI getHolidaysUri(LocalDateTime now) {
        return uriComponentsBuilder
                .uri(URI.create(API_BASIC_URL))
                .pathSegment(HOLIDAY_URL_PATH)
                .queryParam(YEAR_KEY, now.getYear())
                .queryParam(MONTH_KEY, String.format(MONTH_FORMAT, now.getMonthValue()))
                .queryParam(TYPE_KEY, RESPONSE_TYPE_JSON)
                .queryParam(SERVICE_KEY_KEY, SERVICE_KEY_VALUE)
                .build(true)
                .toUri();
    }
}
