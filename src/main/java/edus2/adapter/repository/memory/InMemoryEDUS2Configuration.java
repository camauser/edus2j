package edus2.adapter.repository.memory;

import edus2.domain.EDUS2Configuration;

import java.io.File;
import java.util.Optional;

public class InMemoryEDUS2Configuration implements EDUS2Configuration {
    private Integer minimumVideoHeight;
    private Integer minimumVideoWidth;
    private String hashedPassword;
    private String saveFileLocation;
    private String defaultScenarioDirectory;

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
    public Optional<String> getSaveFileLocation() {
        return Optional.ofNullable(saveFileLocation);
    }

    @Override
    public Optional<File> getDefaultScenarioDirectory() {
        if (defaultScenarioDirectory == null) {
            return Optional.empty();
        }
        return Optional.of(new File(defaultScenarioDirectory));
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
    public void setSaveFileLocation(String saveFileLocation) {
        this.saveFileLocation = saveFileLocation;
    }

    @Override
    public void setDefaultScenarioDirectory(File defaultScenarioDirectory) {
        this.defaultScenarioDirectory = defaultScenarioDirectory.getAbsolutePath();
    }
}
