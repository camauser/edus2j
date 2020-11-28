package edus2.adapter.repository.file;

import edus2.domain.EDUS2Configuration;
import edus2.domain.EDUS2ConfigurationTest;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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

    @Test
    public void constructor_unexpectedFileFormat() throws IOException {
        // Arrange
        Path path = Files.createTempFile("file", "edus2configurationtest");
        String contents = "invalid JSON";
        Files.write(path, contents.getBytes());

        // Act
        ThrowingRunnable task = () -> new FileEDUS2Configuration(path.getParent().toString(), path.getFileName().toString());

        // Assert
        RuntimeException actual = assertThrows(RuntimeException.class, task);
        assertEquals(String.format("Couldn't parse config file. Contents: \"%s\"", contents), actual.getMessage());
    }
}