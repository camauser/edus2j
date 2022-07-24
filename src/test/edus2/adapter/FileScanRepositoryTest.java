package edus2.adapter;

import edus2.adapter.repository.file.FileScanRepository;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;
import edus2.domain.ScanRepositoryTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static edus2.TestUtil.Lst;
import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.assertEquals;

public class FileScanRepositoryTest extends ScanRepositoryTest {

    private File file;

    @Override
    protected ScanRepository getScanRepository() {
        String fileName = randomAlphanumericString();
        file = new File(fileName);
        file.deleteOnExit();
        InMemoryEDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setSaveFileLocation(file.getAbsolutePath());
        return new FileScanRepository(configuration);
    }

    @Test
    public void retrieveAll_legacyFileFormat() throws IOException {
        // Arrange
        Files.write(file.toPath(), "{\"scans\": \"[{\\\"scanEnum\\\":\\\"CARDIAC_SC\\\",\\\"path\\\":\\\"file:///C:/edus2j/problematic[file].mp4\\\"}]\"}".getBytes());

        // Act
        List<Scan> actual = scanRepository.retrieveAll();

        // Assert
        assertEquals(Lst(new Scan(ManikinScanEnum.CARDIAC_SC, Paths.get("C:/edus2j/problematic[file].mp4"))), actual);
    }

    @Test
    public void retrieveAll_legacyFileFormatWithSpaces() throws IOException {
        // Arrange
        Files.write(file.toPath(), "{\"scans\": \"[{\\\"scanEnum\\\":\\\"CARDIAC_SC\\\",\\\"path\\\":\\\"file:///C:/edus2j/problematic%20file.mp4\\\"}]\"}".getBytes());

        // Act
        List<Scan> actual = scanRepository.retrieveAll();

        // Assert
        assertEquals(Lst(new Scan(ManikinScanEnum.CARDIAC_SC, Paths.get("C:/edus2j/problematic file.mp4"))), actual);
    }
}