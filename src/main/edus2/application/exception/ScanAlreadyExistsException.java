package edus2.application.exception;

import edus2.domain.MannequinScanEnum;

public class ScanAlreadyExistsException extends RuntimeException {
    public ScanAlreadyExistsException(MannequinScanEnum scanEnum) {
        super(String.format("A scan already exists for %s", scanEnum.getName()));
    }
}
