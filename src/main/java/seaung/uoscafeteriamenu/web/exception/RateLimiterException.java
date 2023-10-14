package seaung.uoscafeteriamenu.web.exception;

public class RateLimiterException extends RuntimeException {

    public static String TOO_MANY_REQUEST = "조금 천천히 물어봐라. 휴.먼";

    public RateLimiterException() {
    }

    public RateLimiterException(String message) {
        super(message);
    }

    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimiterException(Throwable cause) {
        super(cause);
    }

    public RateLimiterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
