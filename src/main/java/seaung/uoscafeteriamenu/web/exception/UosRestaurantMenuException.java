package seaung.uoscafeteriamenu.web.exception;

public class UosRestaurantMenuException extends IllegalArgumentException {

    public static String NOT_FOUND_MENU = "예외 발생.... 식당 메뉴가 없다. 휴.먼.친구.";

    public UosRestaurantMenuException() {
        super();
    }

    public UosRestaurantMenuException(String s) {
        super(s);
    }

    public UosRestaurantMenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public UosRestaurantMenuException(Throwable cause) {
        super(cause);
    }
}
