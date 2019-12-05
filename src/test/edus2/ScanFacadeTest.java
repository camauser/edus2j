package edus2;

import edus2.adapter.repository.file.FileScanRepository;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.application.ScanFacade;
import edus2.application.exception.EmptyScanIdException;
import edus2.application.exception.ScanAlreadyExistsException;
import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static edus2.TestUtil.*;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class ScanFacadeTest {

    private ScanFacade scanFacade;
    private Scan scan;
    private Scan scanTwo;

    @Before
    public void setup() {
        InMemoryEDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        configuration.setSaveFileLocation(randomTempFile());
        scanFacade = new ScanFacade(new FileScanRepository(configuration));
        scan = new Scan(MannequinScanEnum.RIGHT_LUNG, randomAlphanumericString());
        scanTwo = new Scan(MannequinScanEnum.LEFT_LUNG, randomAlphanumericString());
    }

    @Test
    public void getScan_shouldReturnEmpty_whenScanDoesNotExist() {
        // Assert
        assertEquals(Optional.empty(), scanFacade.getScan(randomMannequinScanEnum()));
    }

    @Test
    public void getScan_shouldReturnScan_whenScanExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        Optional<Scan> actual = scanFacade.getScan(scan.getScanEnum());

        // Assert
        assertEquals(scan, actual.get());
    }

    @Test
    public void containsScan_shouldReturnFalse_whenScanDoesNotExist() {
        // Act
        // Assert
        assertFalse(scanFacade.containsScan(randomMannequinScanEnum()));
    }

    @Test
    public void containsScan_shouldReturnTrue_whenScanExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        // Assert
        assertTrue(scanFacade.containsScan(scan.getScanEnum()));
    }

    @Test
    public void addScan_shouldSaveScan() {
        /// Act
        scanFacade.addScan(scan);

        // Assert
        assertTrue(scanFacade.containsScan(scan.getScanEnum()));
    }

    @Test (expected = ScanAlreadyExistsException.class)
    public void addScan_shouldThrowScanAlreadyExistsException_whenScanAlreadyExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        scanFacade.addScan(scan);
    }

    @Test (expected = EmptyScanIdException.class)
    public void addScan_shouldThrowEmptyScanIdException_whenEmptyScanIdGiven() {
        // Arrange
        Scan emptyScanIdScan = new Scan(null, randomAlphanumericString());

        // Act
        scanFacade.addScan(emptyScanIdScan);

    }

    @Test
    public void addScans_shouldAddAllScans() {
        // Act
        scanFacade.addScans(Lst(scan, scanTwo));

        // Assert
        assertTrue(scanFacade.containsScan(scan.getScanEnum()));
        assertTrue(scanFacade.containsScan(scanTwo.getScanEnum()));
    }

    @Test
    public void removeScan_shouldRemoveScan_whenScanPresent() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.containsScan(scan.getScanEnum()));
    }

    @Test
    public void removeScan_shouldLeaveOtherScansUntouched() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.containsScan(scan.getScanEnum()));
        assertTrue(scanFacade.containsScan(scanTwo.getScanEnum()));
    }

    @Test
    public void removeAllScans_shouldRemoveAllScans() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeAllScans();

        // Assert
        assertEquals(0, scanFacade.scanCount());
    }

    @Test
    public void getAllScans_shouldReturnAllScans_whenScansPresent() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        List<Scan> actual = scanFacade.getAllScans();

        // Assert
        assertTrue(actual.contains(scan));
        assertTrue(actual.contains(scanTwo));
        assertEquals(2, actual.size());
    }

    @Test
    public void getAllScans_shouldReturnEmptyList_whenNoScansPresent() {
        // Act
        List<Scan> actual = scanFacade.getAllScans();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void ToCSV_shouldCorrectlySerializeScans() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        String actual = scanFacade.toCSV();

        // Assert
        assertEquals(scan.getScanEnum() + "," + scan.getPath() + "\n" + scanTwo.getScanEnum() + "," + scanTwo.getPath() + "\n", actual);
    }

    @Test
    public void importCSV_shouldImportAllScanFromCSV() {
        // Act
        scanFacade.importCSV(scan.getScanEnum() + "," + scan.getPath() + "\n" + scanTwo.getScanEnum() + "," + scanTwo.getPath() + "\n");

        // Assert
        assertEquals(scan, scanFacade.getScan(scan.getScanEnum()).get());
        assertEquals(scanTwo, scanFacade.getScan(scanTwo.getScanEnum()).get());
    }

}