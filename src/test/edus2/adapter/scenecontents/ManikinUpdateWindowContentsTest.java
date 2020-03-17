package edus2.adapter.scenecontents;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryManikinRepository;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ManikinUpdateWindowContentsTest extends SceneContentsTest {

    private ManikinFacade manikinFacade;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        manikinFacade = new ManikinFacade(new InMemoryManikinRepository());
        SceneBuilder sceneBuilder = new SceneBuilder(new InMemoryEDUS2Configuration());
        ManikinUpdateWindowContents manikinUpdateWindowContents = new ManikinUpdateWindowContents(sceneBuilder, manikinFacade);

        Manikin manikin = new Manikin(generateTagMap(), "Manikin");
        manikinFacade.create(manikin);
        manikinUpdateWindowContents.bindManikin(manikin);

        scene = manikinUpdateWindowContents.getScene();
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void saveManikin_shouldUpdateScanTags() {
        // Arrange
        TextField leftLungField = from(scene.getRoot()).lookup(".text-field").nth(1).query();
        leftLungField.setText("a");

        // Act
        clickOn(point("Save Manikin"));

        // Assert
        assertEquals("a", manikinFacade.getManikin("Manikin").get().getTagMap().get(ManikinScanEnum.LEFT_LUNG));
    }

    @Test
    public void saveManikin_shouldDisplayError_whenDuplicateTagGiven() {
        // Arrange
        TextField leftLungField = from(scene.getRoot()).lookup(".text-field").nth(1).query();
        TextField rightLungField = from(scene.getRoot()).lookup(".text-field").nth(2).query();
        leftLungField.setText("a");
        rightLungField.setText("a");

        // Act
        clickOn(point("Save Manikin"));

        // Assert
        assertPopupContents("Error saving manikin: Scan tag a was listed more than once");
        assertEquals("0", manikinFacade.getManikin("Manikin").get().getTagMap().get(ManikinScanEnum.LEFT_LUNG));
        assertEquals("1", manikinFacade.getManikin("Manikin").get().getTagMap().get(ManikinScanEnum.RIGHT_LUNG));
    }

    private Map<ManikinScanEnum, String> generateTagMap() {
        Map<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.LEFT_LUNG, "0");
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, "1");
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, "2");
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, "3");
        tagMap.put(ManikinScanEnum.CARDIAC_SC, "4");
        tagMap.put(ManikinScanEnum.IVC, "5");
        tagMap.put(ManikinScanEnum.RUQ, "6");
        tagMap.put(ManikinScanEnum.LUQ, "7");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "8");
        tagMap.put(ManikinScanEnum.PELVIS, "9");
        return tagMap;
    }
}