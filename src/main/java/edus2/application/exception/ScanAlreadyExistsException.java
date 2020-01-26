package edus2.application.exception;

import edus2.domain.ManikinScanEnum;

public class ScanAlreadyExistsException extends RuntimeException {
    public ScanAlreadyExistsException(ManikinScanEnum scanEnum) {
        super(String.format("A scan already exists for %s", scanEnum.getName()));
    }
}
