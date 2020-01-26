package edus2;

import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import org.junit.Test;

import static edus2.TestUtil.randomAlphanumericString;
import static edus2.TestUtil.randomManikinScanEnum;
import static org.junit.Assert.assertEquals;

public class ScanTest {

    @Test
    public void getScanEnum_shouldReturnLocationOfScan() {
        // Arrange
        Scan scan = new Scan(ManikinScanEnum.RIGHT_LUNG, randomAlphanumericString());

        // Act
        ManikinScanEnum actual = scan.getScanEnum();

        // Assert
        assertEquals(ManikinScanEnum.RIGHT_LUNG, actual);
    }

    @Test
    public void getPath_shouldReturnPathOfScan() {
        // Arrange
        Scan scan = new Scan(randomManikinScanEnum(), "sample/path");

        // Act
        String actual = scan.getPath();

        // Assert
        assertEquals("sample/path", actual);
    }

}