package edus2;

import edus2.domain.Scan;
import org.junit.Test;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.assertEquals;

public class ScanTest {

    @Test
    public void getId_shouldReturnIdOfScan() {
        // Arrange
        Scan scan = new Scan("scanId", randomAlphanumericString());

        // Act
        String actual = scan.getId();

        // Assert
        assertEquals("scanId", actual);
    }

    @Test
    public void getPath_shouldReturnPathOfScan() {
        // Arrange
        Scan scan = new Scan(randomAlphanumericString(), "sample/path");

        // Act
        String actual = scan.getPath();

        // Assert
        assertEquals("sample/path", actual);
    }

}