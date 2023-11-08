package seaung.uoscafeteriamenu.web.exception;

public class SpecialHolidayException extends HolidayException {
    public SpecialHolidayException(String message) {
        super(message);
    }

    public SpecialHolidayException(String message, Throwable cause) {
        super(message, cause);
    }
}
