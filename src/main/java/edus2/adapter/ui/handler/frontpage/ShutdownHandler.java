package edus2.adapter.ui.handler.frontpage;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

public class ShutdownHandler extends FrontpageHandler {
    private final Stage stage;
    private final ExecutorService threadPool;

    public ShutdownHandler(BorderPane mainDisplayPane, Stage stage, ExecutorService threadPool) {
        super(mainDisplayPane);
        this.stage = stage;
        this.threadPool = threadPool;
    }

    @Override
    protected void handleRequest() {
        mainDisplayPane.requestFocus();
        stage.close();
        handleShutdown();
    }

    private void handleShutdown() {
        threadPool.shutdownNow();
    }
}
