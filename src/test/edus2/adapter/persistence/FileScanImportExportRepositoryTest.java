package edus2.adapter.persistence;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edus2.adapter.repository.file.FileScanImportExportRepository;
import edus2.adapter.repository.file.dto.ScanDto;
import edus2.adapter.repository.memory.InMemoryScanRepository;
import edus2.application.ScanFacade;
import edus2.domain.ManikinScanEnum;
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
import static org.junit.Assert.*;

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
        Files.write(Paths.get(fileName), "[{\"scanEnum\": \"RIGHT_LUNG\", \"path\": \"/path/to/scan\"}, {\"scanEnum\": \"LEFT_LUNG\", \"path\": \"/path/to/second/scan\"}]".getBytes());

        // Act
        repository.importScansFromFile(new File(fileName));

        // Assert
        List<Scan> scans = scanFacade.getAllScans();
        assertEquals(2, scans.size());
        assertTrue(scans.contains(new Scan(ManikinScanEnum.RIGHT_LUNG, Paths.get("/path/to/scan"))));
        assertTrue(scans.contains(new Scan(ManikinScanEnum.LEFT_LUNG, Paths.get("/path/to/second/scan"))));
    }

    @Test
    public void importScans_shouldClearExistingScans() throws IOException {
        // Arrange
        Scan unexpectedScan = new Scan(ManikinScanEnum.IVC, Paths.get("scan/to/delete"));
        scanFacade.addScan(unexpectedScan);
        String fileName = randomTempFile();
        Files.write(Paths.get(fileName), "[{\"scanEnum\": \"RIGHT_LUNG\", \"path\": \"/path/to/scan\"}, {\"scanEnum\": \"LEFT_LUNG\", \"path\": \"/path/to/second/scan\"}]".getBytes());

        // Act
        repository.importScansFromFile(new File(fileName));

        // Assert
        List<Scan> scans = scanFacade.getAllScans();
        assertEquals(2, scans.size());
        assertFalse(scans.contains(unexpectedScan));
    }

    @Test
    public void exportScans_shouldExportScansToFile() throws IOException {
        // Arrange
        Gson gson = new Gson();
        String fileName = randomTempFile();
        Scan firstScan = new Scan(ManikinScanEnum.RIGHT_LUNG, Paths.get("/scan/path"));
        Scan secondScan = new Scan(ManikinScanEnum.LEFT_LUNG, Paths.get("/second/path"));
        scanFacade.addScans(Lst(firstScan, secondScan));

        // Act
        repository.exportScansToFile(new File(fileName));

        // Assert
        List<ScanDto> actual = gson.fromJson(new String(Files.readAllBytes(Paths.get(fileName))), new TypeToken<List<ScanDto>>() {
        }.getType());
        assertEquals(Lst(new ScanDto(firstScan), new ScanDto(secondScan)), actual);
    }

    @Test
    public void exportImport_endToEnd() throws IOException {
        // Arrange
        String fileName = randomTempFile();
        List<Scan> scans = Lst(new Scan(ManikinScanEnum.RIGHT_LUNG, Paths.get("/scan/path")), new Scan(ManikinScanEnum.LEFT_LUNG, Paths.get("/second/path")));
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