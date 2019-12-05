package edus2.adapter.persistence;

import edus2.adapter.repository.memory.InMemoryScanRepository;
import edus2.application.ScanFacade;
import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static edus2.TestUtil.Lst;
import static edus2.TestUtil.randomTempFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileScanImportExportRepositoryTest {

    private ScanFacade scanFacade;
    private FileScanImportExportRepository repository;

    @Before
    public void setup() {
        scanFacade = new ScanFacade(new InMemoryScanRepository());
        repository = new FileScanImportExportRepository(scanFacade);
    }

    @Test
    public void importScans_shouldImportScansInFile() throws IOException {
        // Arrange
        String fileName = randomTempFile();
        Files.write(Paths.get(fileName), "[{\"id\": \"RIGHT_LUNG\", \"path\": \"/path/to/scan\"}, {\"id\": \"secondScan\", \"LEFT_LUNG\": \"/path/to/second/scan\"}]".getBytes());

        // Act
        repository.importScansFromFile(new File(fileName));

        // Assert
        List<Scan> scans = scanFacade.getAllScans();
        assertEquals(2, scans.size());
        assertTrue(scans.contains(new Scan(MannequinScanEnum.RIGHT_LUNG, "/path/to/scan")));
        assertTrue(scans.contains(new Scan(MannequinScanEnum.LEFT_LUNG, "/path/to/second/scan")));
    }

    @Test
    public void exportScans_shouldExportScansToFile() throws IOException {
        // Arrange
        String fileName = randomTempFile();
        scanFacade.addScans(Lst(new Scan(MannequinScanEnum.RIGHT_LUNG, "/scan/path"), new Scan(MannequinScanEnum.LEFT_LUNG, "/second/path")));

        // Act
        repository.exportScansToFile(new File(fileName));

        // Assert
        String result = new String(Files.readAllBytes(Paths.get(fileName)));
        assertEquals("[{\"id\":\"RIGHT_LUNG\",\"path\":\"/scan/path\"},{\"id\":\"LEFT_LUNG\",\"path\":\"/second/path\"}]", result);
    }

    @Test
    public void exportImport_endToEnd() throws IOException {
        // Arrange
        String fileName = randomTempFile();
        List<Scan> scans = Lst(new Scan(MannequinScanEnum.RIGHT_LUNG, "/scan/path"), new Scan(MannequinScanEnum.LEFT_LUNG, "/second/path"));
        scanFacade.addScans(scans);
        repository.exportScansToFile(new File(fileName));
        scanFacade.removeAllScans();

        // Act
        repository.importScansFromFile(new File(fileName));

        // Assert
        List<Scan> actual = scanFacade.getAllScans();
        assertEquals(scans, actual);
    }
}