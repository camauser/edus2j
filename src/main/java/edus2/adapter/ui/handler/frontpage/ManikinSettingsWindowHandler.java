package edus2.adapter.ui.handler.frontpage;

import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ManikinSettingsWindow;
import edus2.application.ManikinFacade;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class ManikinSettingsWindowHandler extends FrontpageHandler {
    private final ManikinFacade manikinFacade;

    public ManikinSettingsWindowHandler(BorderPane mainDisplayPane, ManikinFacade manikinFacade) {
        super(mainDisplayPane);
        this.manikinFacade = manikinFacade;
    }

    @Override
    protected void handleRequest() {
        try {
            mainDisplayPane.requestFocus();
            EDUS2IconStage manikinSettingStage = new EDUS2IconStage();
            ManikinSettingsWindow manikinSettingsWindow = new ManikinSettingsWindow(manikinFacade, manikinSettingStage);
            Scene manikinWindowScene = new Scene(manikinSettingsWindow);
            manikinSettingStage.setScene(manikinWindowScene);
            manikinSettingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("An error has occurred: %s", e.getMessage()));
            alert.showAndWait();
        }
    }
}
