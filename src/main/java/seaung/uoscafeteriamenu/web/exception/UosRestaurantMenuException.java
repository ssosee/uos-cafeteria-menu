package seaung.uoscafeteriamenu.web.exception;

public class UosRestaurantMenuException extends IllegalArgumentException {

    public static String NOT_FOUND_MENU = "예외 발생.... 학교에서 메뉴를 제공하지 않았다.. 휴.먼.친구.";
    public static String NOT_FOUND_RESTAURANT = "예외 발생.... 지원하지 않는 식당이다. 휴.먼.친구.";
    public static String NOT_FOUND_MEAL_TYPE = "예외 발생.... 지원하지 않는 식사종류다. 휴.먼.친구.";

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
