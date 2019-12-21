package edus2.adapter.repository.file;

public class MissingConfigurationFileException extends RuntimeException {
    public MissingConfigurationFileException(String message) {
        super(message);
    }
}
