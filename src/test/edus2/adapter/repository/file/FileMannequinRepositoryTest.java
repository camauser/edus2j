package edus2.adapter.repository.file;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.domain.EDUS2Configuration;
import edus2.domain.MannequinRepository;
import edus2.domain.MannequinRepositoryTest;

import java.io.File;

import static edus2.TestUtil.randomAlphanumericString;

public class FileMannequinRepositoryTest extends MannequinRepositoryTest {

    @Override
    public MannequinRepository getMannequinRepository() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        EDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setSaveFileLocation(file.getAbsolutePath());
        return new FileMannequinRepository(configuration);
    }
}