package edus2.adapter.repository.file;

import edus2.domain.EDUS2Configuration;
import edus2.domain.EDUS2ConfigurationTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileEDUS2ConfigurationTest extends EDUS2ConfigurationTest {

    @Override
    public EDUS2Configuration getConfiguration() {
        try {
            Path path = Files.createTempFile("file", "edus2configurationtest");
            return new FileEDUS2Configuration(path.getParent().toString(), path.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}