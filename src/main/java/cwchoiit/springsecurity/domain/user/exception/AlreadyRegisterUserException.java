package cwchoiit.springsecurity.domain.user.exception;

public class AlreadyRegisterUserException extends RuntimeException {

    public AlreadyRegisterUserException() {
        super("User already exists.");
    }

    public AlreadyRegisterUserException(String message) {
        super(message);
    }

    public AlreadyRegisterUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyRegisterUserException(Throwable cause) {
        super(cause);
    }

    public AlreadyRegisterUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
