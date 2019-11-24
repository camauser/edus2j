package edus2.domain;

import java.util.Optional;

public interface EDUS2Configuration {
     Optional<Integer> getMinimumVideoHeight();

     Optional<Integer> getMinimumVideoWidth();

     Optional<String> getHashedPassword();

     Optional<String> getSaveFileLocation();

     void setMinimumVideoHeight(int minimumVideoHeight);

     void setMinimumVideoWidth(int minimumVideoWidth);

     void setHashedPassword(String hashedPassword);

     void setSaveFileLocation(String saveFileLocation);
}
