package edus2.adapter;

import edus2.adapter.repository.file.FileScanRepository;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.domain.ScanRepository;
import edus2.domain.ScanRepositoryTest;

import java.io.File;

import static edus2.TestUtil.randomAlphanumericString;

public class FileScanRepositoryTest extends ScanRepositoryTest {

    @Override
    protected ScanRepository getScanRepository() {
        String fileName = randomAlphanumericString();
        File file = new File(fileName);
        file.deleteOnExit();
        InMemoryEDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setScanFileLocation(file.getAbsolutePath());
        return new FileScanRepository(configuration);
    }
}