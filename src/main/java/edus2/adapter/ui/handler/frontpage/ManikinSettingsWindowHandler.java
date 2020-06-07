package edus2.adapter.ui.handler.frontpage;

import com.google.inject.Inject;
import edus2.adapter.stagebuilder.ManikinSettingsWindowContents;
import edus2.adapter.ui.EDUS2IconStage;
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
            EDUS2IconStage manikinSettingStage = manikinSettingsWindowContents.build();
            manikinSettingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("An error has occurred: %s", e.getMessage()));
            alert.showAndWait();
        }
    }
}
