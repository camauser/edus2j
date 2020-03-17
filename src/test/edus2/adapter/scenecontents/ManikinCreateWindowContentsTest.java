package edus2.adapter.scenecontents;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryManikinRepository;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edus2.TestUtil.randomAlphanumericString;
import static org.junit.Assert.assertEquals;

public class ManikinCreateWindowContentsTest extends SceneContentsTest {

    private ManikinFacade manikinFacade;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        manikinFacade = new ManikinFacade(new InMemoryManikinRepository());
        SceneBuilder sceneBuilder = new SceneBuilder(new InMemoryEDUS2Configuration());
        ManikinCreateWindowContents createWindowContents = new ManikinCreateWindowContents(sceneBuilder, manikinFacade);
        scene = createWindowContents.getScene();
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void saveManikin_shouldCreateManikin_whenEntryValid() {
        // Arrange
        populateScanPoints("Manny", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        Button submitButton = from(scene.getRoot()).lookup(".button").query();

        // Act
        clickOn(submitButton);

        // Assert
        List<Manikin> actual = manikinFacade.getAllManikins();
        assertEquals(1, actual.size());
    }

    @Test
    public void saveManikin_shouldDisplayError_whenManikinNameExists() {
        // Arrange
        manikinFacade.create(new Manikin(generateTagMap(), "Manny"));
        populateScanPoints("Manny", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        Button submitButton = from(scene.getRoot()).lookup(".button").query();

        // Act
        clickOn(submitButton);

        // Assert
        assertPopupContents("Error saving manikin: A manikin named Manny already exists!");
    }

    @Test
    public void saveManikin_shouldDisplayError_whenScanTagPresentOnExistingManikin() {
        // Arrange
        String duplicateTag = "IVC";
        Map<ManikinScanEnum, String> tagMap = generateTagMap();
        tagMap.put(ManikinScanEnum.IVC, duplicateTag);
        manikinFacade.create(new Manikin(tagMap, "Manny"));

        populateScanPoints("New Manny", duplicateTag, "2", "3", "4", "5", "6", "7", "8", "9", "10");
        Button submitButton = from(scene.getRoot()).lookup(".button").query();

        // Act
        clickOn(submitButton);

        // Assert
        assertPopupContents("Error saving manikin: Scan tag 'IVC' already exists on manikin 'Manny'");
    }

    @Test
    public void saveManikin_shouldDisplayError_whenDuplicateTagGiven() {
        // Arrange
        String duplicateTag = "duplicate";
        populateScanPoints("Manny", duplicateTag, duplicateTag, "3", "4", "5", "6", "7", "8", "9", "10");
        Button submitButton = from(scene.getRoot()).lookup(".button").query();

        // Act
        clickOn(submitButton);

        // Assert
        assertPopupContents("Error saving manikin: Scan tag duplicate was listed more than once");
    }

    @Test
    public void saveManikin_shouldDisplayError_whenScanPointEmpty() {
        // Arrange
        populateScanPoints("Manny", null, "2", "3", "4", "5", "6", "7", "8", "9", "10");
        Button submitButton = from(scene.getRoot()).lookup(".button").query();

        // Act
        clickOn(submitButton);

        // Assert+
        assertPopupContents("Error saving manikin: One or more locations are missing a scan tag.");
    }

    private void populateScanPoints(String name, String leftLung, String rightLung, String pslPss, String a4c, String sc, String ivc,
                                    String luq, String ruq, String aorta, String pelvis) {
        setText(0, name);
        setText(1, leftLung);
        setText(2, rightLung);
        setText(3, pslPss);
        setText(4, a4c);
        setText(5, sc);
        setText(6, ivc);
        setText(7, luq);
        setText(8, ruq);
        setText(9, aorta);
        setText(10, pelvis);
    }

    private void setText(int textBoxIndex, String text) {
        TextField nameField = from(scene.getRoot()).lookup(".text-field").nth(textBoxIndex).query();
        nameField.setText(text);
    }

    private Map<ManikinScanEnum, String> generateTagMap() {
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.LEFT_LUNG, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.CARDIAC_SC, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.IVC, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.RUQ, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.LUQ, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, randomAlphanumericString());
        tagMap.put(ManikinScanEnum.PELVIS, randomAlphanumericString());
        return tagMap;
    }

}