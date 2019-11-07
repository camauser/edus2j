package edus2.adapter.repository.memory;

import edus2.domain.EDUS2Configuration;

import java.util.Optional;

public class InMemoryEDUS2Configuration implements EDUS2Configuration {
    private Integer minimumVideoHeight;
    private Integer minimumVideoWidth;
    private String hashedPassword;
    private String scanFileLocation;

    @Override
    public Optional<Integer> getMinimumVideoHeight() {
        return Optional.ofNullable(minimumVideoHeight);
    }

    @Override
    public Optional<Integer> getMinimumVideoWidth() {
        return Optional.ofNullable(minimumVideoWidth);
    }

    @Override
    public Optional<String> getHashedPassword() {
        return Optional.ofNullable(hashedPassword);
    }

    @Override
    public Optional<String> getScanFileLocation() {
        return Optional.ofNullable(scanFileLocation);
    }

    @Override
    public void setMinimumVideoHeight(int minimumVideoHeight) {
        this.minimumVideoHeight = minimumVideoHeight;
    }

    @Override
    public void setMinimumVideoWidth(int minimumVideoWidth) {
        this.minimumVideoWidth = minimumVideoWidth;
    }

    @Override
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public void setScanFileLocation(String scanFileLocation) {
        this.scanFileLocation = scanFileLocation;
    }
}
