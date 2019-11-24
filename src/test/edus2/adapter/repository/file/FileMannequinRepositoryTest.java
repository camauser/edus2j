package edus2.adapter.repository.file;

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
        return new FileMannequinRepository(file.getAbsolutePath());
    }
}