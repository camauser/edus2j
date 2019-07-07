package edus2.application.exception;

public class EmptyScanIdException extends RuntimeException {
    public EmptyScanIdException() {
        super("Empty scan ID given.");
    }
}
