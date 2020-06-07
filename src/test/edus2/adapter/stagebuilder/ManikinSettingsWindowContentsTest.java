package edus2.adapter.stagebuilder;

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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ManikinSettingsWindowContentsTest extends StageBuilderTest {
    private static final int LEFT_LUNG_BOX = 1;
    private static final int RIGHT_LUNG_BOX = 2;
    private static final int PSL_PSS_BOX = 3;
    private static final int A4C_BOX = 4;
    private static final int SC_BOX = 5;
    private static final int IVC_BOX = 6;
    private static final int LUQ_BOX = 7;
    private static final int RUQ_BOX = 8;
    private static final int AA_BOX = 9;
    private static final int PELVIS_BOX = 10;

    private ManikinFacade manikinFacade;
    private FileManikinImportExportRepository importExportRepositoryMock;

    @Override
    public void start(Stage stage) {
        SceneBuilder sceneBuilder = new SceneBuilder(new InMemoryEDUS2Configuration());
        manikinFacade = new ManikinFacade(new InMemoryManikinRepository());
        Manikin manikin = new Manikin(generateTagMap(), "Manny");
        manikinFacade.create(manikin);
        importExportRepositoryMock = mock(FileManikinImportExportRepository.class);
        ManikinUpdateWindowContents updateWindowContents = new ManikinUpdateWindowContents(sceneBuilder, manikinFacade);

        ManikinSettingsWindowContents manikinSettingsWindowContents = spy(
                new ManikinSettingsWindowContents(sceneBuilder, manikinFacade, new EDUS2IconStage(),
                        new ManikinsWindow(manikinFacade), importExportRepositoryMock, null,
                        updateWindowContents)
        );

        doReturn(new File("C:/openFile")).when(manikinSettingsWindowContents).promptForKinFileOpen();
        doReturn(new File("C:/saveFile")).when(manikinSettingsWindowContents).promptForKinFileSave();
        Scene scene = manikinSettingsWindowContents.buildScene();
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
    public void updateScanTags_shouldCorrectlyBindManikin() {
        // Act
        clickOn(point("Manny")).clickOn(point("Update Scan Tags"));

        // Assert
        assertEquals("0", lookup(".text-field").nth(LEFT_LUNG_BOX).<TextField>query().getText());
        assertEquals("1", lookup(".text-field").nth(RIGHT_LUNG_BOX).<TextField>query().getText());
        assertEquals("2", lookup(".text-field").nth(PSL_PSS_BOX).<TextField>query().getText());
        assertEquals("3", lookup(".text-field").nth(A4C_BOX).<TextField>query().getText());
        assertEquals("4", lookup(".text-field").nth(SC_BOX).<TextField>query().getText());
        assertEquals("5", lookup(".text-field").nth(IVC_BOX).<TextField>query().getText());
        assertEquals("6", lookup(".text-field").nth(LUQ_BOX).<TextField>query().getText());
        assertEquals("7", lookup(".text-field").nth(RUQ_BOX).<TextField>query().getText());
        assertEquals("8", lookup(".text-field").nth(AA_BOX).<TextField>query().getText());
        assertEquals("9", lookup(".text-field").nth(PELVIS_BOX).<TextField>query().getText());

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
        tagMap.put(ManikinScanEnum.LUQ, "6");
        tagMap.put(ManikinScanEnum.RUQ, "7");
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, "8");
        tagMap.put(ManikinScanEnum.PELVIS, "9");
        return tagMap;
    }

}