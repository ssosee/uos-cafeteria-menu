package seaung.uoscafeteriamenu.web.exception;

public class WeekendException extends RuntimeException {

    public static String NOT_PROVIDE_MENU_AT_WEEKEND = "삐..삐비빅..\n주말에는 나도 에너.지 🪫🔋충전한다.\n평일에 와라...\n휴.먼";

    public WeekendException(String message) {
        super(message);
    }

    public WeekendException(String message, Throwable cause) {
        super(message, cause);
    }
}
