package edus2.adapter.ui.handler.frontpage;

import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.PasswordInputDialog;
import edus2.adapter.ui.ScanSettingsWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.AuthenticationFacade;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class ScanSettingsWindowHandler extends FrontpageHandler {
    private final AuthenticationFacade authenticationFacade;
    private final ScanFacade scanFacade;
    private final SceneBuilder sceneBuilder;
    private final EDUS2Configuration configuration;

    public ScanSettingsWindowHandler(BorderPane mainDisplayPane, AuthenticationFacade authenticationFacade,
                                     ScanFacade scanFacade, SceneBuilder sceneBuilder, EDUS2Configuration configuration) {
        super(mainDisplayPane);
        this.authenticationFacade = authenticationFacade;
        this.scanFacade = scanFacade;
        this.sceneBuilder = sceneBuilder;
        this.configuration = configuration;
    }

    @Override
    protected void handleRequest() {
        try {
            if (isAuthenticated()) {
                EDUS2IconStage scanWindowStage = new EDUS2IconStage();
                ScanSettingsWindow scanSettingsWindow = new ScanSettingsWindow(scanFacade, authenticationFacade, configuration, sceneBuilder, scanWindowStage);
                Scene scanWindowScene = sceneBuilder.build(scanSettingsWindow);
                scanWindowStage.setScene(scanWindowScene);
                scanWindowStage.show();
            } else {
                Alert invalidPasswordAlert = new Alert(Alert.AlertType.ERROR);
                invalidPasswordAlert.setTitle("Invalid password");
                invalidPasswordAlert.setContentText("Invalid password entered.");
                invalidPasswordAlert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("An error has occurred: %s", e.getMessage()));
            alert.showAndWait();
        }
    }

    private boolean isAuthenticated() {
        if (!authenticationFacade.isAuthenticationEnabled()) {
            return true;
        }

        Optional<String> passwordAttemptOptional = promptForPassword();

        if (!passwordAttemptOptional.isPresent()) {
            return false;
        }
        return authenticationFacade.isValidLogin(passwordAttemptOptional.get());
    }

    private Optional<String> promptForPassword() {
        PasswordInputDialog passwordEntryBox = new PasswordInputDialog("Enter Password", "Please enter password to continue");
        return passwordEntryBox.showAndWait();
    }
}
