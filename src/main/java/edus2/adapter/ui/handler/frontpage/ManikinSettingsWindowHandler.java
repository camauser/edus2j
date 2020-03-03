package edus2.adapter.ui.handler.frontpage;

import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ManikinSettingsWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

public class ManikinSettingsWindowHandler extends FrontpageHandler {
    private final SceneBuilder sceneBuilder;
    private final ManikinFacade manikinFacade;

    public ManikinSettingsWindowHandler(BorderPane mainDisplayPane, SceneBuilder sceneBuilder, ManikinFacade manikinFacade) {
        super(mainDisplayPane);
        this.sceneBuilder = sceneBuilder;
        this.manikinFacade = manikinFacade;
    }

    @Override
    protected void handleRequest() {
        try {
            mainDisplayPane.requestFocus();
            EDUS2IconStage manikinSettingStage = new EDUS2IconStage();
            ManikinSettingsWindow manikinSettingsWindow = new ManikinSettingsWindow(manikinFacade, sceneBuilder, manikinSettingStage);
            Scene manikinWindowScene = sceneBuilder.build(manikinSettingsWindow);
            manikinSettingStage.setScene(manikinWindowScene);
            manikinSettingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("An error has occurred: %s", e.getMessage()));
            alert.showAndWait();
        }
    }
}
