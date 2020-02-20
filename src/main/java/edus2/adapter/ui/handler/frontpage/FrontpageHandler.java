package edus2.adapter.ui.handler.frontpage;

import javafx.scene.layout.BorderPane;

public abstract class FrontpageHandler {
    protected BorderPane mainDisplayPane;

    public FrontpageHandler(BorderPane mainDisplayPane) {
        this.mainDisplayPane = mainDisplayPane;
    }
    public final void handle() {
        handleRequest();
        mainDisplayPane.requestFocus();
    }

    protected abstract void handleRequest();
}
