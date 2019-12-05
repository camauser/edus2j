package edus2;

import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;
import org.junit.Test;

import static edus2.TestUtil.randomAlphanumericString;
import static edus2.TestUtil.randomMannequinScanEnum;
import static org.junit.Assert.assertEquals;

public class ScanTest {

    @Test
    public void getScanEnum_shouldReturnLocationOfScan() {
        // Arrange
        Scan scan = new Scan(MannequinScanEnum.RIGHT_LUNG, randomAlphanumericString());

        // Act
        MannequinScanEnum actual = scan.getScanEnum();

        // Assert
        assertEquals(MannequinScanEnum.RIGHT_LUNG, actual);
    }

    @Test
    public void getPath_shouldReturnPathOfScan() {
        // Arrange
        Scan scan = new Scan(randomMannequinScanEnum(), "sample/path");

        // Act
        String actual = scan.getPath();

        // Assert
        assertEquals("sample/path", actual);
    }

}