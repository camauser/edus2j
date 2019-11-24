package edus2.adapter.repository.memory;

import edus2.domain.MannequinRepository;
import edus2.domain.MannequinRepositoryTest;

public class InMemoryMannequinRepositoryTest extends MannequinRepositoryTest {

    @Override
    public MannequinRepository getMannequinRepository() {
        return new InMemoryMannequinRepository();
    }
}