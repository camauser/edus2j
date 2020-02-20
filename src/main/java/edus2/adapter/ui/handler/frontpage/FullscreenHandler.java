package edus2.adapter.ui.handler.frontpage;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FullscreenHandler extends FrontpageHandler {

    private final Stage stage;

    public FullscreenHandler(BorderPane mainDisplayPane, Stage stage) {
        super(mainDisplayPane);
        this.stage = stage;
    }
    @Override
    protected void handleRequest() {
        stage.setFullScreen(!stage.isFullScreen());
    }
}
