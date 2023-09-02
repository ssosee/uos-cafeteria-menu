package seaung.uoscafeteriamenu.web.exception;

public class MenuLikeException extends IllegalArgumentException {

    public static String CONFLICT_MENU = "이미 추천했다.\n휴.먼.친구.";

    public MenuLikeException() {
        super();
    }

    public MenuLikeException(String s) {
        super(s);
    }

    public MenuLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuLikeException(Throwable cause) {
        super(cause);
    }
}
