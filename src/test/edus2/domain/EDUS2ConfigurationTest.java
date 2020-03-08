package edus2.domain;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

import static edus2.TestUtil.randomInt;
import static org.junit.Assert.*;

public abstract class EDUS2ConfigurationTest {
    private EDUS2Configuration configuration;

    public abstract EDUS2Configuration getConfiguration();
    
    @Before
    public void setup() {
        this.configuration = getConfiguration();
    }
    
    @Test
    public void getMinimumVideoHeight_shouldReturnEmpty_whenHeightNotSet() {
        // Act
        Optional<Integer> actual = configuration.getMinimumVideoHeight();
        
        // Assert
        assertFalse(actual.isPresent());
    }
    
    @Test
    public void getMinimumVideoHeight_shouldReturnValue_whenValueSaved() {
        // Arrange
        int expected = randomInt();
        configuration.setMinimumVideoHeight(expected);

        // Act
        Optional<Integer> actual = configuration.getMinimumVideoHeight();

        // Assert
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void getMinimumVideoWidth_shouldReturnEmpty_whenWidthNotSet() {
        // Act
        Optional<Integer> actual = configuration.getMinimumVideoWidth();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getMinimumVideoWidth_shouldReturnValue_whenValueSaved() {
        // Arrange
        int expected = randomInt();
        configuration.setMinimumVideoWidth(expected);

        // Act
        Optional<Integer> actual = configuration.getMinimumVideoWidth();

        // Assert
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void getHashedPassword_shouldReturnEmpty_whenNoPasswordSet() {
        // Act
        Optional<String> actual = configuration.getHashedPassword();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getHashedPassword_shouldReturnHashedPassword_whenPasswordSet() {
        // Arrange
        configuration.setHashedPassword("hashedPassword");

        // Act
        Optional<String> actual = configuration.getHashedPassword();

        // Assert
        assertEquals(Optional.of("hashedPassword"), actual);
    }

    @Test
    public void getSaveFileLocation_shouldReturnEmpty_whenNoLocationSpecified() {
        // Act
        Optional<String> actual = configuration.getSaveFileLocation();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getSaveFileLocation_shouldReturnScanLocation_whenLocationSet() {
        // Arrange
        configuration.setSaveFileLocation("file/scan/location.json");

        // Act
        Optional<String> actual = configuration.getSaveFileLocation();

        // Assert
        assertEquals(Optional.of("file/scan/location.json"), actual);
    }

    @Test
    public void getDefaultScenarioDirectory_shouldReturnEmpty_whenLocationNotSet() {
        // Act
        Optional<File> actual = configuration.getDefaultScenarioDirectory();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getDefaultScenarioDirectory_shouldReturnScenarioDirectory_whenDirectorySet() {
        // Arrange
        File expected = new File("C:\\scenario\\directory");
        configuration.setDefaultScenarioDirectory(expected);

        // Act
        Optional<File> actual = configuration.getDefaultScenarioDirectory();

        // Assert
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void setDefaultScenarioDirectory_shouldOverwriteDirectory_whenDirectoryAlreadySet() {
        // Arrange
        File expected = new File("C:\\expected\\directory");
        configuration.setDefaultScenarioDirectory(new File("C:\\first\\directory"));

        // Act
        configuration.setDefaultScenarioDirectory(expected);

        // Assert
        Optional<File> actual = configuration.getDefaultScenarioDirectory();
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void getDefaultVideoDirectory_shouldReturnEmpty_whenLocationNotSet() {
        // Act
        Optional<File> actual = configuration.getDefaultVideoDirectory();

        // Assert
        assertFalse(actual.isPresent());
    }

    @Test
    public void getDefaultVideoDirectory_shouldReturnVideoDirectory_whenDirectorySet() {
        // Arrange
        File expected = new File("C:\\video\\directory");
        configuration.setDefaultVideoDirectory(expected);

        // Act
        Optional<File> actual = configuration.getDefaultVideoDirectory();

        // Assert
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void setDefaultVideoDirectory_shouldOverwriteDirectory_whenDirectoryAlreadySet() {
        // Arrange
        File expected = new File("C:\\expected\\directory");
        configuration.setDefaultScenarioDirectory(new File("C:\\first\\directory"));

        // Act
        configuration.setDefaultVideoDirectory(expected);

        // Assert
        Optional<File> actual = configuration.getDefaultVideoDirectory();
        assertEquals(Optional.of(expected), actual);
    }

    @Test
    public void acceptedPhoneHomeWarning_shouldInitiallyReturnFalse() {
        // Act
        boolean actual = configuration.acceptedPhoneHomeWarning();

        // Assert
        assertFalse(actual);
    }

    @Test
    public void acceptPhoneHomeWarning_shouldSetPhoneHomeWarningShownToTrue() {
        // Act
        configuration.acceptPhoneHomeWarning();

        // Assert
        boolean actual = configuration.acceptedPhoneHomeWarning();
        assertTrue(actual);
    }

    @Test
    public void acceptPhoneHomeWarning_shouldDoNothing_whenCalledMultipleTimes() {
        // Arrange
        configuration.acceptPhoneHomeWarning();

        // Act
        configuration.acceptPhoneHomeWarning();

        // Assert
        boolean actual = configuration.acceptedPhoneHomeWarning();
        assertTrue(actual);
    }

    @Test
    public void darkModeEnabledProperty_shouldInitiallyReturnTrue() {
        // Act
        boolean actual = configuration.darkModeEnabledProperty().get();

        // Assert
        assertTrue(actual);
    }

    @Test
    public void setDarkModeEnabled_shouldSetDarkModeEnabledSwitch() {
        // Act
        configuration.setDarkModeEnabled(false);

        // Assert
        boolean actual = configuration.darkModeEnabledProperty().get();
        assertFalse(actual);
    }
}