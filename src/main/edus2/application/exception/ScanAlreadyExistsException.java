package edus2.application.exception;

public class ScanAlreadyExistsException extends RuntimeException {
    public ScanAlreadyExistsException(String scanId) {
        super(String.format("A scan with ID %s already exists", scanId));
    }
}
