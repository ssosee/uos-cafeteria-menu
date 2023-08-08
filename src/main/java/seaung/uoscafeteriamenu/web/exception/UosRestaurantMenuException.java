package seaung.uoscafeteriamenu.web.exception;

public class UosRestaurantMenuException extends IllegalArgumentException {

    public static String NOT_FOUND_MENU = "해당 식당 메뉴가 없습니다.";

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
