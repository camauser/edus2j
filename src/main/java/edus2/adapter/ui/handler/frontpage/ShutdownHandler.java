package edus2.adapter.ui.handler.frontpage;

import edus2.adapter.ui.ListenableMediaPlayer;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;

public class ShutdownHandler extends FrontpageHandler {
    private final Stage stage;
    private final ExecutorService threadPool;
    private final ListenableMediaPlayer listenableMediaPlayer;

    public ShutdownHandler(BorderPane mainDisplayPane, Stage stage, ExecutorService threadPool,
                           ListenableMediaPlayer listenableMediaPlayer) {
        super(mainDisplayPane);
        this.stage = stage;
        this.threadPool = threadPool;
        this.listenableMediaPlayer = listenableMediaPlayer;
    }

    @Override
    protected void handleRequest() {
        mainDisplayPane.requestFocus();
        stage.close();
        handleShutdown();
    }

    private void handleShutdown() {
        listenableMediaPlayer.release();
        threadPool.shutdownNow();
    }
}
