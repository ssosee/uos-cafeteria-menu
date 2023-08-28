package seaung.uoscafeteriamenu.web.exception;

public class ApikeyException extends IllegalArgumentException {

    public static String VALID_API_KEY_CODE = "[code: 1000]";

    public ApikeyException() {
        super();
    }

    public ApikeyException(String s) {
        super(s);
    }

    public ApikeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApikeyException(Throwable cause) {
        super(cause);
    }
}
