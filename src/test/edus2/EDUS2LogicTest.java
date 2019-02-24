package edus2;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static edus2.TestUtil.randomAlphanumericString;
import static edus2.TestUtil.randomScan;
import static edus2.Util.Lst;
import static org.junit.Assert.*;

public class EDUS2LogicTest {

    private EDUS2Logic edus2Logic;
    private Scan scan;

    @Before
    public void setup() {
        edus2Logic = new EDUS2Logic();
        scan = randomScan();
    }

    @Test
    public void getScan_shouldReturnEmpty_whenScanDoesNotExist() {
        // Assert
        assertEquals(Optional.empty(), edus2Logic.getScan(randomAlphanumericString()));
    }

    @Test
    public void getScan_shouldReturnScan_whenScanExists() {
        // Arrange
        edus2Logic.addScan(scan);

        // Act
        Optional<Scan> actual = edus2Logic.getScan(scan.getId());

        // Assert
        assertEquals(scan, actual.get());
    }

    @Test
    public void containsScan_shouldReturnFalse_whenScanDoesNotExist() {
        // Act
        // Assert
        assertFalse(edus2Logic.containsScan(randomAlphanumericString()));
    }

    @Test
    public void containsScan_shouldReturnTrue_whenScanExists() {
        // Arrange
        edus2Logic.addScan(scan);

        // Act
        // Assert
        assertTrue(edus2Logic.containsScan(scan.getId()));
    }

    @Test
    public void addScan_shouldSaveScan() {
        /// Act
        edus2Logic.addScan(scan);

        // Assert
        assertTrue(edus2Logic.containsScan(scan.getId()));
    }

    @Test
    public void addScans_shouldAddAllScans() {
        // Arrange
        Scan scanTwo = randomScan();

        // Act
        edus2Logic.addScans(Lst(scan, scanTwo));

        // Assert
        assertTrue(edus2Logic.containsScan(scan.getId()));
        assertTrue(edus2Logic.containsScan(scanTwo.getId()));
    }

    @Test
    public void removeScan_shouldRemoveScan_whenScanPresent() {
        // Arrange
        edus2Logic.addScan(scan);

        // Act
        edus2Logic.removeScan(scan);

        // Assert
        assertFalse(edus2Logic.containsScan(scan.getId()));
    }

    @Test
    public void removeScan_shouldLeaveOtherScansUntouched() {
        // Arrange
        Scan scanTwo = randomScan();
        edus2Logic.addScans(Lst(scan, scanTwo));

        // Act
        edus2Logic.removeScan(scan);

        // Assert
        assertFalse(edus2Logic.containsScan(scan.getId()));
        assertTrue(edus2Logic.containsScan(scanTwo.getId()));
    }

    @Test
    public void removeAllScans_shouldRemoveAllScans() {
        // Arrange
        Scan scanTwo = randomScan();
        edus2Logic.addScans(Lst(scan, scanTwo));

        // Act
        edus2Logic.removeAllScans();

        // Assert
        assertEquals(0, edus2Logic.scanCount());
    }

    @Test
    public void getAllScans_shouldReturnAllScans_whenScansPresent() {
        // Arrange
        Scan scanTwo = randomScan();
        edus2Logic.addScans(Lst(scan, scanTwo));

        // Act
        ArrayList<Scan> actual = edus2Logic.getAllScans();

        // Assert
        assertTrue(actual.contains(scan));
        assertTrue(actual.contains(scanTwo));
        assertEquals(2, actual.size());
    }

    @Test
    public void getAllScans_shouldReturnEmptyList_whenNoScansPresent() {
        // Act
        ArrayList<Scan> actual = edus2Logic.getAllScans();

        // Assert
        assertTrue(actual.isEmpty());
    }

    @Test
    public void ToCSV_shouldCorrectlySerializeScans() {
        // Arrange
        Scan scanTwo = randomScan();
        edus2Logic.addScans(Lst(scan, scanTwo));

        // Act
        String actual = edus2Logic.toCSV();

        // Assert
        assertEquals(scan.getId() + "," + scan.getPath() + "\n" + scanTwo.getId() + "," + scanTwo.getPath() + "\n", actual);
    }

    @Test
    public void importCSV_shouldImportAllScanFromCSV() {
        // Arrange
        Scan scanTwo = randomScan();

        // Act
        edus2Logic.importCSV(scan.getId() + "," + scan.getPath() + "\n" + scanTwo.getId() + "," + scanTwo.getPath() + "\n");

        // Assert
        assertEquals(scan, edus2Logic.getScan(scan.getId()).get());
        assertEquals(scanTwo, edus2Logic.getScan(scanTwo.getId()).get());
    }

}