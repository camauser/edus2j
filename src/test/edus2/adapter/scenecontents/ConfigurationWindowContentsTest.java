package edus2.adapter.scenecontents;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.AuthenticationFacade;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ConfigurationWindowContentsTest extends SceneContentsTest {

    private static final int MIN_VIDEO_WIDTH_INDEX = 0;
    private static final int MIN_VIDEO_HEIGHT_INDEX = 1;
    private static final int SET_PASSWORD_BUTTON_INDEX = 0;
    private static final int CLEAR_PASSWORD_BUTTON_INDEX = 1;
    private InMemoryEDUS2Configuration configuration;
    private Scene scene;
    private File expectedDirectory;
    private File expectedFile;

    @Override
    public void start(Stage stage) {
        SceneBuilder sceneBuilder = new SceneBuilder(new InMemoryEDUS2Configuration());
        configuration = new InMemoryEDUS2Configuration();
        AuthenticationFacade authenticationFacade = new AuthenticationFacade(configuration);
        ConfigurationWindowContents configurationWindowContents = spy(new ConfigurationWindowContents(sceneBuilder, configuration, authenticationFacade, new EDUS2IconStage()));
        expectedDirectory = new File("C:/directory");
        expectedFile = new File("C:/file");
        doReturn(expectedDirectory).when(configurationWindowContents).promptForDirectory();
        doReturn(expectedFile).when(configurationWindowContents).promptForFile();

        scene = configurationWindowContents.getScene();
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void minimumWidthTextbox_shouldNotAcceptNonNumericCharacters() {
        // Arrange
        TextField minimumWidthField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_WIDTH_INDEX).query();

        // Act
        clickOn(minimumWidthField).write("abcd");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoWidth();
        assertFalse(actual.isPresent());
    }

    @Test
    public void minimumHeightTextbox_shouldNotAcceptNonNumericCharacters() {
        // Arrange
        TextField minimumHeightField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_HEIGHT_INDEX).query();

        // Act
        clickOn(minimumHeightField).write("abcd");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoHeight();
        assertFalse(actual.isPresent());
    }

    @Test
    public void minimumWidthTextbox_shouldIgnoreNegativeSign() {
        // Arrange
        TextField minimumWidthField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_WIDTH_INDEX).query();

        // Act
        clickOn(minimumWidthField).write("-512");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoWidth();
        assertEquals(Optional.of(512), actual);
    }

    @Test
    public void minimumHeightTextbox_shouldIgnoreNegativeSign() {
        // Arrange
        TextField minimumHeightField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_HEIGHT_INDEX).query();

        // Act
        clickOn(minimumHeightField).write("-862");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoHeight();
        assertEquals(Optional.of(862), actual);
    }

    @Test
    public void minimumWidthTextbox_shouldUpdateMinimumWidthInConfiguration() {
        // Arrange
        TextField minimumWidthField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_WIDTH_INDEX).query();

        // Act
        clickOn(minimumWidthField).write("418");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoWidth();
        assertEquals(Optional.of(418), actual);
    }

    @Test
    public void minimumHeightTextbox_shouldUpdateMinimumHeightInConfiguration() {
        // Arrange
        TextField minimumHeightField = from(scene.getRoot()).lookup(".text-field").nth(MIN_VIDEO_HEIGHT_INDEX).query();

        // Act
        clickOn(minimumHeightField).write("562");

        // Assert
        Optional<Integer> actual = configuration.getMinimumVideoHeight();
        assertEquals(Optional.of(562), actual);
    }

    @Test
    public void setPassword_shouldSetPassword() {
        // Arrange
        String password = "password";
        Button passwordButton = from(scene.getRoot()).lookup(".button").nth(SET_PASSWORD_BUTTON_INDEX).query();
        clickOn(passwordButton).write(password).type(KeyCode.ENTER).write(password).type(KeyCode.ENTER);

        // Act
        Optional<String> actual = configuration.getHashedPassword();

        // Assert
        assertEquals(Optional.of(hash(password)), actual);
    }

    @Test
    public void setPassword_shouldThrowError_whenPasswordConfirmationInvalid() {
        // Arrange
        Button passwordButton = from(scene.getRoot()).lookup(".button").nth(SET_PASSWORD_BUTTON_INDEX).query();
        clickOn(passwordButton).write("a").type(KeyCode.ENTER).write("b").type(KeyCode.ENTER);

        // Act
        Optional<String> actual = configuration.getHashedPassword();

        // Assert
        assertPopupContents("Password and password confirmation didn't match!");
        assertFalse(actual.isPresent());
    }

    @Test
    public void clearPassword_shouldClearPassword_whenPasswordSet() {
        // Arrange
        configuration.setHashedPassword(hash("password"));
        Button clearPasswordButton = from(scene.getRoot()).lookup(".button").nth(CLEAR_PASSWORD_BUTTON_INDEX).query();

        // Act
        clickOn(clearPasswordButton);

        // Assert
        assertFalse(configuration.getHashedPassword().isPresent());
    }

    @Test
    public void darkModeSwitch_shouldDisableDarkMode_whenUnchecked() {
        // Act
        clickOn(point("Enable"));

        // Assert
        assertFalse(configuration.darkModeEnabledProperty().get());
    }
    
    @Test
    public void setDefaultScenarioDirectory_shouldSetDefaultScenarioDirectory() {
        // Act
        clickOn(point("Set Default Scenario Directory"));
        
        // Assert
        Optional<File> actual = configuration.getDefaultScenarioDirectory();
        assertEquals(Optional.of(expectedDirectory), actual);
    }

    @Test
    public void setDefaultVideoDirectory_shouldSetDefaultVideoDirectory() {
        // Act
        clickOn(point("Set Default Video Directory"));

        // Assert
        Optional<File> actual = configuration.getDefaultVideoDirectory();
        assertEquals(Optional.of(expectedDirectory), actual);
    }

    @Test
    public void setSaveFileLocation_shouldSetSaveFileLocation() {
        // Act
        clickOn(point("Set Save File Location"));

        // Assert
        Optional<String> actual = configuration.getSaveFileLocation();
        assertEquals(Optional.of(expectedFile.getAbsolutePath()), actual);
    }

    private String hash(String text) {
        return DigestUtils.sha256Hex(text);
    }

}