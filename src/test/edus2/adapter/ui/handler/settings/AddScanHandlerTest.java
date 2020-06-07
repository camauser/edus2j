package edus2.adapter.ui.handler.settings;

import edus2.adapter.repository.memory.InMemoryEDUS2Configuration;
import edus2.adapter.repository.memory.InMemoryScanRepository;
import edus2.adapter.stagebuilder.StageBuilderTest;
import edus2.application.ScanFacade;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.File;

import static edus2.TestUtil.Lst;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class AddScanHandlerTest extends StageBuilderTest {

    private ScanFacade scanFacade;
    private AddScanHandler handler;

    @Override
    public void start(Stage stage) {
        scanFacade = new ScanFacade(new InMemoryScanRepository());
        handler = spy(new AddScanHandler(scanFacade, new InMemoryEDUS2Configuration()));
        doReturn(new File("C:/scan")).when(handler).promptForScanFile(any());
        stage.setScene(new Scene(new VBox()));
        stage.show();
    }

    @Test
    public void handle_shouldPromptForScanLocation() throws InterruptedException {
        // Arrange
        Platform.runLater(() -> handler.handle(Lst(), null));
        Thread.sleep(250);
        DialogPane popupDialogPane = getPopupDialogPane();
        ComboBox<String> scanLocationBox = from(popupDialogPane).lookup(".combo-box").query();

        // Act
        clickOn(scanLocationBox).clickOn(point("IVC")).clickOn("OK");

        // Assert
        assertEquals("file:///C:/scan", scanFacade.getScan(ManikinScanEnum.IVC).get().getPath());
    }

    @Test
    public void handle_shouldNotShowUsedLocations() throws InterruptedException {
        // Arrange
        scanFacade.addScan(new Scan(ManikinScanEnum.IVC, "C:/path"));
        Platform.runLater(() -> handler.handle(Lst(), null));
        Thread.sleep(250);
        DialogPane popupDialogPane = getPopupDialogPane();
        ComboBox<String> scanLocationBox = from(popupDialogPane).lookup(".combo-box").query();

        // Act
        ObservableList<String> actual = scanLocationBox.getItems();

        // Assert
        assertFalse(actual.contains("IVC"));
        assertEquals(ManikinScanEnum.values().length - 1, actual.size());
    }
}