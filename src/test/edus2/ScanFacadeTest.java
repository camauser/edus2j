package edus2;

import edus2.adapter.FileScanRepository;
import edus2.application.ScanFacade;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static edus2.TestUtil.randomScan;
import static edus2.TestUtil.randomTempFile;
import static edus2.application.Util.Lst;
import static org.junit.Assert.*;

@SuppressWarnings("ConstantConditions")
public class ScanFacadeTest {

    private ScanFacade scanFacade;
    private Scan scan;

    @Before
    public void setup() {
        // TODO: Inject scanFacade
        scanFacade = new ScanFacade(new FileScanRepository(randomTempFile()));
        scan = randomScan();
    }

    @Test
    public void getScan_shouldReturnEmpty_whenScanDoesNotExist() {
        // Assert
        assertEquals(Optional.empty(), scanFacade.getScan(randomAlphanumericString()));
    }

    @Test
    public void getScan_shouldReturnScan_whenScanExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        Optional<Scan> actual = scanFacade.getScan(scan.getId());

        // Assert
        assertEquals(scan, actual.get());
    }

    @Test
    public void containsScan_shouldReturnFalse_whenScanDoesNotExist() {
        // Act
        // Assert
        assertFalse(scanFacade.containsScan(randomAlphanumericString()));
    }

    @Test
    public void containsScan_shouldReturnTrue_whenScanExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        // Assert
        assertTrue(scanFacade.containsScan(scan.getId()));
    }

    @Test
    public void addScan_shouldSaveScan() {
        /// Act
        scanFacade.addScan(scan);

        // Assert
        assertTrue(scanFacade.containsScan(scan.getId()));
    }

    @Test
    public void addScans_shouldAddAllScans() {
        // Arrange
        Scan scanTwo = randomScan();

        // Act
        scanFacade.addScans(Lst(scan, scanTwo));

        // Assert
        assertTrue(scanFacade.containsScan(scan.getId()));
        assertTrue(scanFacade.containsScan(scanTwo.getId()));
    }

    @Test
    public void removeScan_shouldRemoveScan_whenScanPresent() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.containsScan(scan.getId()));
    }

    @Test
    public void removeScan_shouldLeaveOtherScansUntouched() {
        // Arrange
        Scan scanTwo = randomScan();
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.containsScan(scan.getId()));
        assertTrue(scanFacade.containsScan(scanTwo.getId()));
    }

    @Test
    public void removeAllScans_shouldRemoveAllScans() {
        // Arrange
        Scan scanTwo = randomScan();
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeAllScans();

        // Assert
        assertEquals(0, scanFacade.scanCount());
    }

    @Test
    public void getAllScans_shouldReturnAllScans_whenScansPresent() {
        // Arrange
        Scan scanTwo = randomScan();
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
        Scan scanTwo = randomScan();
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        String actual = scanFacade.toCSV();

        // Assert
        assertEquals(scan.getId() + "," + scan.getPath() + "\n" + scanTwo.getId() + "," + scanTwo.getPath() + "\n", actual);
    }

    @Test
    public void importCSV_shouldImportAllScanFromCSV() {
        // Arrange
        Scan scanTwo = randomScan();

        // Act
        scanFacade.importCSV(scan.getId() + "," + scan.getPath() + "\n" + scanTwo.getId() + "," + scanTwo.getPath() + "\n");

        // Assert
        assertEquals(scan, scanFacade.getScan(scan.getId()).get());
        assertEquals(scanTwo, scanFacade.getScan(scanTwo.getId()).get());
    }

}