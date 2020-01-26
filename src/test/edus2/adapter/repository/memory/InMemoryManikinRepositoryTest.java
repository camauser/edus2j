package edus2.adapter.repository.memory;

import edus2.domain.ManikinRepository;
import edus2.domain.ManikinRepositoryTest;

public class InMemoryManikinRepositoryTest extends ManikinRepositoryTest {

    @Override
    public ManikinRepository getManikinRepository() {
        return new InMemoryManikinRepository();
    }
}