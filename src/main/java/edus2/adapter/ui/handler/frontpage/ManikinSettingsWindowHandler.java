package edus2.adapter.ui.handler.frontpage;

import com.google.inject.Inject;
import edus2.adapter.scenecontents.ManikinSettingsWindowContents;
import edus2.adapter.ui.EDUS2IconStage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class ManikinSettingsWindowHandler extends FrontpageHandler {
    private final ManikinSettingsWindowContents manikinSettingsWindowContents;

    @Inject
    public ManikinSettingsWindowHandler(BorderPane mainDisplayPane, ManikinSettingsWindowContents manikinSettingsWindowContents) {
        super(mainDisplayPane);
        this.manikinSettingsWindowContents = manikinSettingsWindowContents;
    }

    @Override
    protected void handleRequest() {
        try {
            mainDisplayPane.requestFocus();
            Scene scene = manikinSettingsWindowContents.getScene();
            EDUS2IconStage manikinSettingStage = new EDUS2IconStage();
            manikinSettingStage.setScene(scene);
            manikinSettingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("An error has occurred: %s", e.getMessage()));
            alert.showAndWait();
        }
    }
}
