package edus2.domain;

import edus2.domain.property.ReadableObserableProperty;

import java.io.File;
import java.util.Optional;

public interface EDUS2Configuration {
    Optional<Integer> getMinimumVideoHeight();

    Optional<Integer> getMinimumVideoWidth();

    Optional<String> getHashedPassword();

    Optional<String> getSaveFileLocation();

    Optional<File> getDefaultScenarioDirectory();

    Optional<File> getDefaultVideoDirectory();

    ReadableObserableProperty<Boolean> darkModeEnabledProperty();

    void setMinimumVideoHeight(int minimumVideoHeight);

    void setMinimumVideoWidth(int minimumVideoWidth);

    void setHashedPassword(String hashedPassword);

    void setSaveFileLocation(String saveFileLocation);

    void setDefaultScenarioDirectory(File defaultScenarioDirectory);

    void setDefaultVideoDirectory(File defaultVideoDirectory);

    void setDarkModeEnabled(boolean enabled);
}
