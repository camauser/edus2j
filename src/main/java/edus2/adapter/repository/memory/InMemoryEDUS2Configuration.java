package edus2.adapter.repository.memory;

import edus2.domain.EDUS2Configuration;
import edus2.domain.property.ObservableProperty;
import edus2.domain.property.ReadableObserableProperty;

import java.io.File;
import java.util.Optional;

public class InMemoryEDUS2Configuration implements EDUS2Configuration {
    private Integer minimumVideoHeight;
    private Integer minimumVideoWidth;
    private String hashedPassword;
    private String saveFileLocation;
    private String defaultScenarioDirectory;
    private String defaultVideoDirectory;
    private ObservableProperty<Boolean> darkModeEnabledProperty;

    public InMemoryEDUS2Configuration() {
        darkModeEnabledProperty = new ObservableProperty<>(true);
    }

    @Override
    public Optional<Integer> getMinimumVideoHeight() {
        return Optional.ofNullable(minimumVideoHeight);
    }

    @Override
    public Optional<Integer> getMinimumVideoWidth() {
        return Optional.ofNullable(minimumVideoWidth);
    }

    @Override
    public Optional<Integer> getMaximumVideoHeight() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getMaximumVideoWidth() {
        return Optional.empty();
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
    public Optional<File> getDefaultVideoDirectory() {
        if (defaultVideoDirectory == null) {
            return Optional.empty();
        }
        return Optional.of(new File(defaultVideoDirectory));
    }

    @Override
    public ReadableObserableProperty<Boolean> darkModeEnabledProperty() {
        return darkModeEnabledProperty;
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

    @Override
    public void setDefaultVideoDirectory(File defaultVideoDirectory) {
        this.defaultVideoDirectory = defaultVideoDirectory.getAbsolutePath();
    }

    @Override
    public void setDarkModeEnabled(boolean enabled) {
        darkModeEnabledProperty.set(enabled);
    }
}
