package edus2.application.exception;

public class AuthenticationDisabledException extends RuntimeException {
    public AuthenticationDisabledException(String message) {
        super(message);
    }
}
