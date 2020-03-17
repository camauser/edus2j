package edus2.adapter.scenecontents;

import edus2.adapter.repository.file.FileManikinImportExportRepository;
import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryManikinRepository;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ManikinsWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ManikinSettingsWindowContentsTest extends SceneContentsTest {

    private ManikinFacade manikinFacade;
    private FileManikinImportExportRepository importExportRepositoryMock;

    @Override
    public void start(Stage stage) {
        manikinFacade = new ManikinFacade(new InMemoryManikinRepository());
        Manikin manikin = new Manikin(generateTagMap(), "Manny");
        manikinFacade.create(manikin);
        importExportRepositoryMock = mock(FileManikinImportExportRepository.class);

        ManikinSettingsWindowContents manikinSettingsWindowContents =
                spy(new ManikinSettingsWindowContents(new SceneBuilder(new InMemoryEDUS2Configuration()),
                        manikinFacade, new EDUS2IconStage(), new ManikinsWindow(manikinFacade), importExportRepositoryMock,
                        null, null));

        doReturn(new File("C:/openFile")).when(manikinSettingsWindowContents).promptForKinFileOpen();
        doReturn(new File("C:/saveFile")).when(manikinSettingsWindowContents).promptForKinFileSave();
        Scene scene = manikinSettingsWindowContents.getScene();
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void updateName_shouldChangeManikinName() {
        // Act
        clickOn(point("Manny")).clickOn(point("Update Name")).write("New").type(KeyCode.ENTER);

        // Assert
        assertTrue(manikinFacade.getManikin("New").isPresent());
    }

    @Test
    public void delete_shouldRemoveManikin() {
        // Act
        clickOn(point("Manny")).clickOn(point("Delete")).clickOn(point("OK"));

        // Assert
        assertTrue(manikinFacade.getAllManikins().isEmpty());
    }

    @Test
    public void importManikins_shouldStartImportSequence() throws IOException {
        // Act
        clickOn(point("Import Manikins"));

        // Assert
        verify(importExportRepositoryMock, times(1)).importManikinsFromFile(any());
    }

    @Test
    public void exportManikins_shouldStartExportSequence() throws IOException {
        // Act
        clickOn(point("Export Manikins"));

        // Assert
        verify(importExportRepositoryMock, times(1)).exportManikinsToFile(any());
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