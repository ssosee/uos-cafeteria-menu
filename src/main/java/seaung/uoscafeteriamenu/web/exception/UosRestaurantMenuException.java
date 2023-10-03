package seaung.uoscafeteriamenu.web.exception;

public class UosRestaurantMenuException extends IllegalArgumentException {

    public static String NOT_FOUND_MENU = "예외 발생.... 학교에서 메뉴를 제공하지 않았다.. 휴.먼.친구.";
    public static String NOT_FOUND_RESTAURANT = "예외 발생.... 지원하지 않는 식당이다. 휴.먼.친구.";
    public static String NOT_FOUND_MEAL_TYPE = "예외 발생.... 지원하지 않는 식사종류다. 휴.먼.친구.";
    public static String NOT_PROVIDE_MENU_AT_WEEKEND = "삐..삐비빅..\n주말에는 나도 에너.지 🪫🔋충전한다.\n평일에 와라...\n휴.먼";
    public static String CLOSED = "모든 학.교식당 소속 휴.먼들이 에.너지를 🪫충전 하러 갔다.\n우.리 내일. 만나자 휴.먼" +
            "\n\n\n🍳 조식: 08:00 ~ 10:00\n🍜 중식: 11:00 ~ 14:00\n🌙 석식: 17:00 ~ 18:30" +
            "\n\n식당 운영.시간은 약간의 차이가 존재할 수 있다.";

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
