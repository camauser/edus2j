package edus2;

import edus2.adapter.repository.file.FileScanRepository;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.application.ScanFacade;
import edus2.application.exception.EmptyScanIdException;
import edus2.application.exception.ScanAlreadyExistsException;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        scan = new Scan(ManikinScanEnum.RIGHT_LUNG, randomPath());
        scanTwo = new Scan(ManikinScanEnum.LEFT_LUNG, randomPath());
    }

    @Test
    public void getScan_shouldReturnEmpty_whenScanDoesNotExist() {
        // Assert
        assertEquals(Optional.empty(), scanFacade.getScan(randomManikinScanEnum()));
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
    public void addScan_shouldSaveScan() {
        /// Act
        scanFacade.addScan(scan);

        // Assert
        assertTrue(scanFacade.getScan(scan.getScanEnum()).isPresent());
    }

    @Test(expected = ScanAlreadyExistsException.class)
    public void addScan_shouldThrowScanAlreadyExistsException_whenScanAlreadyExists() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        scanFacade.addScan(scan);
    }

    @Test(expected = EmptyScanIdException.class)
    public void addScan_shouldThrowEmptyScanIdException_whenEmptyScanIdGiven() {
        // Arrange
        Scan emptyScanIdScan = new Scan(null, randomPath());

        // Act
        scanFacade.addScan(emptyScanIdScan);

    }

    @Test
    public void addScans_shouldAddAllScans() {
        // Act
        scanFacade.addScans(Lst(scan, scanTwo));

        // Assert
        assertTrue(scanFacade.getScan(scan.getScanEnum()).isPresent());
        assertTrue(scanFacade.getScan(scanTwo.getScanEnum()).isPresent());
    }

    @Test
    public void removeScan_shouldRemoveScan_whenScanPresent() {
        // Arrange
        scanFacade.addScan(scan);

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.getScan(scan.getScanEnum()).isPresent());
    }

    @Test
    public void removeScan_shouldLeaveOtherScansUntouched() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeScan(scan);

        // Assert
        assertFalse(scanFacade.getScan(scan.getScanEnum()).isPresent());
        assertTrue(scanFacade.getScan(scanTwo.getScanEnum()).isPresent());
    }

    @Test
    public void removeAllScans_shouldRemoveAllScans() {
        // Arrange
        scanFacade.addScans(Lst(scan, scanTwo));

        // Act
        scanFacade.removeAllScans();

        // Assert
        assertTrue(scanFacade.getAllScans().isEmpty());
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
    public void getUnusedScanEnums_shouldReturnAllEnums_whenNoScansExist() {
        // Act
        Set<ManikinScanEnum> actual = scanFacade.getUnusedScanEnums();

        // Assert
        assertEquals(ManikinScanEnum.values().length, actual.size());
        assertTrue(actual.containsAll(Arrays.asList(ManikinScanEnum.values())));
    }

    @Test
    public void getUnusedScanEnums_shouldExcludeScanEnum_whenEnumUsed() {
        // Arrange
        scanFacade.addScan(new Scan(ManikinScanEnum.RIGHT_LUNG, randomPath()));

        // Act
        Set<ManikinScanEnum> actual = scanFacade.getUnusedScanEnums();

        // Assert
        assertEquals(ManikinScanEnum.values().length - 1, actual.size());
        assertFalse(actual.contains(ManikinScanEnum.RIGHT_LUNG));
    }

}