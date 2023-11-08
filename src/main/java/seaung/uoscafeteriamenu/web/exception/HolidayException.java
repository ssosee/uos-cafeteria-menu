package seaung.uoscafeteriamenu.web.exception;

public class HolidayException extends RuntimeException {

    public static final String NOT_PROVIDE_MENU_AT_HOLIDAY = "ㅂㅂㅣ.비빅..\n%s은 빨간날 이다...\n평일에 보자...\n휴.먼";

    public HolidayException(String message) {
        super(message);
    }

    public HolidayException(String message, Throwable cause) {
        super(message, cause);
    }
}
