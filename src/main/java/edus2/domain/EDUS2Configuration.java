package edus2.domain;

import java.io.File;
import java.util.Optional;

public interface EDUS2Configuration {
     Optional<Integer> getMinimumVideoHeight();

     Optional<Integer> getMinimumVideoWidth();

     Optional<String> getHashedPassword();

     Optional<String> getSaveFileLocation();

     Optional<File> getDefaultScenarioDirectory();

     SystemIdentifier getSystemIdentifier();

     boolean acceptedPhoneHomeWarning();

     void setMinimumVideoHeight(int minimumVideoHeight);

     void setMinimumVideoWidth(int minimumVideoWidth);

     void setHashedPassword(String hashedPassword);

     void setSaveFileLocation(String saveFileLocation);

     void setDefaultScenarioDirectory(File defaultScenarioDirectory);

     void acceptPhoneHomeWarning();
}
