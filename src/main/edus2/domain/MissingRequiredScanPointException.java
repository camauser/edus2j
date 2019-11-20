package edus2.domain;

public class MissingRequiredScanPointException extends RuntimeException {
    public MissingRequiredScanPointException(String message) {
        super(message);
    }
}
