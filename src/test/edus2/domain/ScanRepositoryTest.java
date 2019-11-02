package edus2.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static edus2.TestUtil.randomScan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class ScanRepositoryTest {

    private ScanRepository scanRepository;

    protected abstract ScanRepository getScanRepository();

    @Before
    public void setup() {
        scanRepository = getScanRepository();
    }

    @Test
    public void retrieveAll_shouldReturnNoScans_whenNoScansExist() {
        List<Scan> actual = scanRepository.retrieveAll();

        assertTrue(actual.isEmpty());
    }

    @Test
    public void retrieveAll_shouldReturnAllScans_whenScansExist() {
        Scan scan = randomScan();
        Scan scanTwo = randomScan();
        scanRepository.save(scan);
        scanRepository.save(scanTwo);

        List<Scan> actual = scanRepository.retrieveAll();

        assertTrue(actual.contains(scan));
        assertTrue(actual.contains(scanTwo));
        assertEquals(2, actual.size());
    }

    @Test
    public void save_shouldSaveScan() {
        Scan scan = randomScan();

        scanRepository.save(scan);

        List<Scan> actual = scanRepository.retrieveAll();
        assertTrue(actual.contains(scan));
        assertEquals(1, actual.size());
    }

    @Test
    public void remove_shouldRemoveScan_whenScanExists() {
        Scan scan = randomScan();
        scanRepository.save(scan);

        scanRepository.remove(scan);

        List<Scan> actual = scanRepository.retrieveAll();
        assertTrue(actual.isEmpty());
    }

    @Test
    public void remove_shouldNotRemoveDuplicateScan_whenSameVideoUnderMultipleIds() {
        Scan scan = randomScan();
        Scan duplicateScan = new Scan(scan.getId() + "-duplicate", scan.getPath());
        scanRepository.save(scan);
        scanRepository.save(duplicateScan);

        scanRepository.remove(duplicateScan);

        List<Scan> actual = scanRepository.retrieveAll();
        assertEquals(1, actual.size());
        assertTrue(actual.contains(scan));
    }

    @Test
    public void remove_shouldNotThrowException_whenScanDoesNotExist() {
        scanRepository.remove(randomScan());
    }

    @Test
    public void removeALl_shouldRemoveAllScans() {
        scanRepository.save(randomScan());
        scanRepository.save(randomScan());

        scanRepository.removeAll();

        List<Scan> actual = scanRepository.retrieveAll();
        assertTrue(actual.isEmpty());
    }

}