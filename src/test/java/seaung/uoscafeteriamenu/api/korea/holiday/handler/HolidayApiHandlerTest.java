package seaung.uoscafeteriamenu.api.korea.holiday.handler;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import seaung.uoscafeteriamenu.api.korea.holiday.response.ApiResponse;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayItem;
import seaung.uoscafeteriamenu.api.korea.holiday.response.HolidayResponse;
import seaung.uoscafeteriamenu.global.provider.TimeProvider;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * 응답 결과가 item의 value가 Object인 경우
 * <pre>
 * {
 *   "response": {
 *     "header": {
 *       "resultCode": "00",
 *       "resultMsg": "NORMAL SERVICE."
 *     },
 *     "body": {
 *       "items": {
 *         "item": {
 *           "dateKind": "01",
 *           "dateName": "기독탄신일",
 *           "isHoliday": "Y",
 *           "locdate": 20231225,
 *           "seq": 1
 *         }
 *       },
 *       "numOfRows": 10,
 *       "pageNo": 1,
 *       "totalCount": 1
 *     }
 *   }
 * }
 * </pre>
 *
 * 응답 결과가 item의 value가 Array인 경우
 * <pre>
 *     {
 *   "response": {
 *     "header": {
 *       "resultCode": "00",
 *       "resultMsg": "NORMAL SERVICE."
 *     },
 *     "body": {
 *       "items": {
 *         "item": [
 *           {
 *             "dateKind": "01",
 *             "dateName": "어린이날",
 *             "isHoliday": "Y",
 *             "locdate": 20230505,
 *             "seq": 1
 *           },
 *           {
 *             "dateKind": "01",
 *             "dateName": "부처님오신날",
 *             "isHoliday": "Y",
 *             "locdate": 20230527,
 *             "seq": 1
 *           },
 *           {
 *             "dateKind": "01",
 *             "dateName": "대체공휴일",
 *             "isHoliday": "Y",
 *             "locdate": 20230529,
 *             "seq": 1
 *           }
 *         ]
 *       },
 *       "numOfRows": 10,
 *       "pageNo": 1,
 *       "totalCount": 3
 *     }
 *   }
 * }
 * </pre>
 */
@SpringBootTest
class HolidayApiHandlerTest {

    @Autowired
    HolidayApiHandler holidayApiHandler;

    @MockBean
    TimeProvider timeProvider;

    @Test
    @DisplayName("시간을 이용하여 공휴일 API를 호출하고 응답 결과가 Array 형태 이다.")
    void callHolidaysApiItemIsObject() {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        // when
        ApiResponse<HolidayResponse> response = holidayApiHandler.callHolidaysApi(timeProvider.getCurrentLocalDateTime());

        // then
        List<HolidayItem> holidays = response.getResponse().getBody().getItems().getItem();
        assertThat(holidays).hasSize(5)
                .extracting("dateKind", "dateName", "isHoliday", "locdate", "seq")
                .contains(
                        tuple("01", "1월1일", "Y", "20230101", 1),
                        tuple("01", "설날", "Y", "20230121", 1),
                        tuple("01", "설날", "Y", "20230122", 1),
                        tuple("01", "설날", "Y", "20230123", 1),
                        tuple("01", "대체공휴일", "Y", "20230124", 1)
                );
    }

    @Test
    @DisplayName("시간을 이용하여 공휴일 API를 호출하고 응답 결과가 Object 형태 이다.")
    void callHolidaysApi() {
        // given
        LocalDateTime fixedDateTime = LocalDateTime.of(2023, 12, 1, 0, 0, 0);
        Mockito.when(timeProvider.getCurrentLocalDateTime()).thenReturn(fixedDateTime);

        // when
        ApiResponse<HolidayResponse> response = holidayApiHandler.callHolidaysApi(timeProvider.getCurrentLocalDateTime());

        // then
        List<HolidayItem> holidays = response.getResponse().getBody().getItems().getItem();
        assertThat(holidays).hasSize(1)
                .extracting("dateKind", "dateName", "isHoliday", "locdate", "seq")
                .contains(
                        tuple("01", "기독탄신일", "Y", "20231225", 1)
                );
    }
}