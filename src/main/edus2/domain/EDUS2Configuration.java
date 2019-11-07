package edus2.domain;

import java.util.Optional;

public interface EDUS2Configuration {
     Optional<Integer> getMinimumVideoHeight();

     Optional<Integer> getMinimumVideoWidth();

     Optional<String> getHashedPassword();

     Optional<String> getScanFileLocation();

     void setMinimumVideoHeight(int minimumVideoHeight);

     void setMinimumVideoWidth(int minimumVideoWidth);

     void setHashedPassword(String hashedPassword);

     void setScanFileLocation(String scanFileLocation);
}
