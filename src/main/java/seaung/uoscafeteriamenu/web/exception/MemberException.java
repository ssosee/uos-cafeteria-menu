package seaung.uoscafeteriamenu.web.exception;

public class MemberException extends IllegalArgumentException {

    public static String NOT_FOUND_MEMBER = "예외 발생.... 회원아이디가 없다. 휴.먼.친구.";

    public MemberException() {
        super();
    }

    public MemberException(String s) {
        super(s);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(Throwable cause) {
        super(cause);
    }
}
