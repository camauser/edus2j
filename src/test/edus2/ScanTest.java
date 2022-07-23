package edus2;

import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static edus2.TestUtil.randomManikinScanEnum;
import static edus2.TestUtil.randomPath;
import static org.junit.Assert.assertEquals;

public class ScanTest {

    @Test
    public void getScanEnum_shouldReturnLocationOfScan() {
        // Arrange
        Scan scan = new Scan(ManikinScanEnum.RIGHT_LUNG, randomPath());

        // Act
        ManikinScanEnum actual = scan.getScanEnum();

        // Assert
        assertEquals(ManikinScanEnum.RIGHT_LUNG, actual);
    }

    @Test
    public void getPath_shouldReturnPathOfScan() {
        // Arrange
        Path expected = Paths.get("sample/path");
        Scan scan = new Scan(randomManikinScanEnum(), expected);

        // Act
        Path actual = scan.getPath();

        // Assert
        assertEquals(expected, actual);
    }

}