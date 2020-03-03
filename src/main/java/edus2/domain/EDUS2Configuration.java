package edus2.domain;

import java.io.File;
import java.util.Optional;

public interface EDUS2Configuration {
     Optional<Integer> getMinimumVideoHeight();

     Optional<Integer> getMinimumVideoWidth();

     Optional<String> getHashedPassword();

     Optional<String> getSaveFileLocation();

     Optional<File> getDefaultScenarioDirectory();

     Optional<File> getDefaultVideoDirectory();

     SystemIdentifier getSystemIdentifier();

     boolean acceptedPhoneHomeWarning();

     boolean darkModeEnabled();

     void setMinimumVideoHeight(int minimumVideoHeight);

     void setMinimumVideoWidth(int minimumVideoWidth);

     void setHashedPassword(String hashedPassword);

     void setSaveFileLocation(String saveFileLocation);

     void setDefaultScenarioDirectory(File defaultScenarioDirectory);

     void setDefaultVideoDirectory(File defaultVideoDirectory);

     void acceptPhoneHomeWarning();

     void setDarkModeEnabled(boolean enabled);

     void registerDarkModeListener(ConfigurationValueListener<Boolean> listener);

     public interface ConfigurationValueListener<T> {
          void onValueChanged(T newValue);
     }
}
