package edus2.adapter;

import edus2.adapter.repository.file.FileScanRepository;
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
        return new FileScanRepository(fileName);
    }
}