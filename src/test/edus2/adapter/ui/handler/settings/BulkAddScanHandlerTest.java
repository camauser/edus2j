package edus2.adapter.ui.handler.settings;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryScanRepository;
import edus2.adapter.stagebuilder.StageBuilderTest;
import edus2.application.ScanFacade;
import edus2.domain.ManikinScanEnum;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;

import static edus2.TestUtil.Lst;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class BulkAddScanHandlerTest extends StageBuilderTest {
    private ScanFacade scanFacade;
    private BulkAddScanHandler handler;

    @Override
    public void start(Stage stage) {
        scanFacade = new ScanFacade(new InMemoryScanRepository());
        handler = spy(new BulkAddScanHandler(scanFacade, new InMemoryEDUS2Configuration()));
        doReturn(Lst(new File("C:/scan"), new File("C:/scan2"))).when(handler).promptForScanFiles(any());
        stage.setScene(new Scene(new VBox()));
        stage.show();
    }

    @Test
    public void handle_shouldHandleMultipleSelectedVideos() throws InterruptedException {
        Platform.runLater(() -> handler.handle(Lst(), null));
        Thread.sleep(250);

        // Act
        chooseScanLocation("Right Lung");
        chooseScanLocation("Left Lung");

        // Assert
        assertEquals("file:///C:/scan", scanFacade.getScan(ManikinScanEnum.RIGHT_LUNG).get().getPath());
        assertEquals("file:///C:/scan2", scanFacade.getScan(ManikinScanEnum.LEFT_LUNG).get().getPath());
    }

    @Test
    public void handle_shouldHandleCancelling() throws InterruptedException {
        // Arrange
        Platform.runLater(() -> handler.handle(Lst(), null));
        Thread.sleep(250);

        // Act
        clickOn(point("Cancel"));

        // Assert
        assertTrue(scanFacade.getAllScans().isEmpty());
    }

    private void chooseScanLocation(String scanLocation) {
        DialogPane popupDialogPane = getPopupDialogPane();
        ComboBox<String> scanLocationBox = from(popupDialogPane).lookup(".combo-box").query();
        clickOn(scanLocationBox).clickOn(point(scanLocation)).clickOn("OK");
    }
}