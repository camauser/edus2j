package edus2.adapter.repository.memory;

import edus2.domain.ScanRepository;
import edus2.domain.ScanRepositoryTest;

public class InMemoryScanRepositoryTest extends ScanRepositoryTest{

    @Override
    protected ScanRepository getScanRepository() {
        return new InMemoryScanRepository();
    }
}