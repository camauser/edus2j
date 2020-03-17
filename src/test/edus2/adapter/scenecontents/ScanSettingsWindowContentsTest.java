package edus2.adapter.scenecontents;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryScanRepository;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ScansWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.settings.*;
import edus2.application.ScanFacade;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ScanSettingsWindowContentsTest extends SceneContentsTest {
    private AddScanHandler addScanHandlerMock;
    private BulkAddScanHandler bulkAddScanHandlerMock;
    private EditManikinLocationHandler editManikinLocationHandlerMock;
    private EditScanFileHandler editScanFileHandlerMock;
    private DeleteScanHandler deleteScanHandlerMock;
    private DeleteAllScanHandler deleteAllScanHandler;
    private LoadScenarioHandler loadScenarioHandlerMock;
    private SaveScenarioHandler saveScenarioHandlerMock;

    @Override
    public void start(Stage stage) {
        ScanFacade scanFacade = new ScanFacade(new InMemoryScanRepository());
        InMemoryEDUS2Configuration configuration = new InMemoryEDUS2Configuration();
        SceneBuilder sceneBuilder = new SceneBuilder(configuration);
        EDUS2IconStage iconStage = new EDUS2IconStage();
        addScanHandlerMock = mock(AddScanHandler.class);
        bulkAddScanHandlerMock = mock(BulkAddScanHandler.class);
        editManikinLocationHandlerMock = mock(EditManikinLocationHandler.class);
        editScanFileHandlerMock = mock(EditScanFileHandler.class);
        deleteScanHandlerMock = mock(DeleteScanHandler.class);
        deleteAllScanHandler = mock(DeleteAllScanHandler.class);
        loadScenarioHandlerMock = mock(LoadScenarioHandler.class);
        saveScenarioHandlerMock = mock(SaveScenarioHandler.class);


        ScanSettingsWindowContents scanSettingsWindowContents = new ScanSettingsWindowContents(sceneBuilder,
                iconStage, new ScansWindow(scanFacade),
                null, addScanHandlerMock, bulkAddScanHandlerMock,
                editManikinLocationHandlerMock, editScanFileHandlerMock, deleteScanHandlerMock, deleteAllScanHandler,
                loadScenarioHandlerMock, saveScenarioHandlerMock);
        Scene scene = scanSettingsWindowContents.getScene();
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void addVideo_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Add Video"));

        // Assert
        verify(addScanHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void bulkAddVideos_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Bulk Add Videos"));

        // Assert
        verify(bulkAddScanHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void editManikinLocation_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Edit Manikin Location"));

        // Assert
        verify(editManikinLocationHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void editVideoPath_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Edit Video Path"));

        // Assert
        verify(editScanFileHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void deleteVideo_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Delete Video"));

        // Assert
        verify(deleteScanHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void deleteAllVideos_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Delete All Videos"));

        // Assert
        verify(deleteAllScanHandler, times(1)).handle(any(), any());
    }

    @Test
    public void loadScenario_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Load Scenario"));

        // Assert
        verify(loadScenarioHandlerMock, times(1)).handle(any(), any());
    }

    @Test
    public void saveScenario_shouldCallCorrectHandler() {
        // Act
        clickOn(point("Save Scenario"));

        // Assert
        verify(saveScenarioHandlerMock, times(1)).handle(any(), any());
    }

}