package edus2.adapter.repository.file;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinRepository;
import edus2.domain.ManikinRepositoryTest;

import java.io.File;

import static edus2.TestUtil.randomAlphanumericString;

public class FileManikinRepositoryTest extends ManikinRepositoryTest {

    @Override
    public ManikinRepository getManikinRepository() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        EDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setSaveFileLocation(file.getAbsolutePath());
        return new FileManikinRepository(configuration);
    }
}