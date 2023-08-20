package seaung.uoscafeteriamenu.web.exception;

public class UosRestaurantMenuException extends IllegalArgumentException {

    public static String NOT_FOUND_MENU = "예외 발생.... 학교에서 메뉴를 제공하지 않았다.. 휴.먼.친구.";
    public static String NOT_FOUND_RESTAURANT = "예외 발생.... 지원하지 않는 식당이다. 휴.먼.친구.";
    public static String NOT_FOUND_MEAL_TYPE = "예외 발생.... 지원하지 않는 식사종류다. 휴.먼.친구.";
    public static String NOT_PROVIDE_MENU_AT_WEEKEND = "삐..삐비빅..\n주말에는 나도 쉰다. 휴.먼...\n우.리 평일에 만나자.";
    public static String CLOSED = "모든 학교식당 영업이 종료되었다.. 휴.먼\n내일 만나자 휴.먼\n\n\n조식: 09:00 ~ 11:00\n중식: 11:00 ~ 14:00\n석식: 17:00 ~ 18:30";

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
