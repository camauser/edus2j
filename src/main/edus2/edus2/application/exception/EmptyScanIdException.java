package edus2.application.exception;

// TODO: Rename and re-evaluate: is this needed?
public class EmptyScanIdException extends RuntimeException {
    public EmptyScanIdException() {
        super("Empty scan ID given.");
    }
}
