package edus2.adapter.ui.handler.frontpage;

import com.google.inject.Inject;
import edus2.adapter.scenecontents.ScanSettingsWindowContents;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.PasswordInputDialog;
import edus2.application.AuthenticationFacade;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;

import java.util.Optional;

public class ScanSettingsWindowHandler extends FrontpageHandler {
    private final AuthenticationFacade authenticationFacade;
    private final ScanSettingsWindowContents scanSettingsWindowContents;

    @Inject
    public ScanSettingsWindowHandler(BorderPane mainDisplayPane, AuthenticationFacade authenticationFacade,
                                     ScanSettingsWindowContents scanSettingsWindowContents) {
        super(mainDisplayPane);
        this.authenticationFacade = authenticationFacade;
        this.scanSettingsWindowContents = scanSettingsWindowContents;
    }

    @Override
    protected void handleRequest() {
        try {
            if (isAuthenticated()) {
                EDUS2IconStage scanWindowStage = new EDUS2IconStage();
                Scene settingsScene = scanSettingsWindowContents.getScene();
                scanWindowStage.setScene(settingsScene);
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
