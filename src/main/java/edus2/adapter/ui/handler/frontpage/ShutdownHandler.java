package edus2.adapter.ui.handler.frontpage;

import edus2.adapter.ui.ScanProgressUpdater;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ShutdownHandler extends FrontpageHandler {
    private final Stage stage;
    private final ScanProgressUpdater scanProgressUpdater;

    public ShutdownHandler(BorderPane mainDisplayPane, Stage stage, ScanProgressUpdater scanProgressUpdater) {
        super(mainDisplayPane);
        this.stage = stage;
        this.scanProgressUpdater = scanProgressUpdater;
    }

    @Override
    protected void handleRequest() {
        mainDisplayPane.requestFocus();
        stage.close();
        handleShutdown();
    }

    private void handleShutdown() {
        scanProgressUpdater.shutdown();
    }
}
