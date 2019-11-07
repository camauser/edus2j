package edus2.adapter.repository.memory;

import edus2.domain.EDUS2Configuration;
import edus2.domain.EDUS2ConfigurationTest;

public class InMemoryEDUS2ConfigurationTest extends EDUS2ConfigurationTest {

    @Override
    public EDUS2Configuration getConfiguration() {
        return new InMemoryEDUS2Configuration();
    }
}