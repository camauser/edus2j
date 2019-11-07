package edus2.adapter.repository.file;

import edus2.domain.EDUS2Configuration;
import edus2.domain.EDUS2ConfigurationTest;

import java.io.File;

import static edus2.TestUtil.randomAlphanumericString;

public class FileEDUS2ConfigurationTest extends EDUS2ConfigurationTest {

    @Override
    public EDUS2Configuration getConfiguration() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        return new FileEDUS2Configuration(fileName);
    }
}