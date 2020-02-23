package edus2.adapter.ui;

import edus2.adapter.ui.builder.FormBuilder;
import edus2.application.AuthenticationFacade;
import edus2.domain.EDUS2Configuration;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ConfigurationWindow extends HBox {
    private final TextField minVideoWidth;
    private final TextField minVideoHeight;
    private EDUS2IconStage stage;
    private EDUS2Configuration configuration;
    private AuthenticationFacade authenticationFacade;

    public ConfigurationWindow(EDUS2Configuration configuration, AuthenticationFacade authenticationFacade, EDUS2IconStage stage) {
        super(10);
        this.authenticationFacade = authenticationFacade;
        this.stage = stage;
        UnaryOperator<TextFormatter.Change> integerFilter = entry -> {
            String text = entry.getText();
            if (text.matches("^\\d*$")) {
                return entry;
            }

            return null;
        };
        this.configuration = configuration;
        minVideoWidth = new TextField();
        minVideoWidth.setTextFormatter(new TextFormatter<>(integerFilter));
        minVideoHeight = new TextField();
        minVideoHeight.setTextFormatter(new TextFormatter<>(integerFilter));

        minVideoWidth.setText(configuration.getMinimumVideoWidth().map(Object::toString).orElse(""));
        minVideoHeight.setText(configuration.getMinimumVideoHeight().map(Object::toString).orElse(""));

        Button btnSetPassword = new Button("Set Password");
        Button btnClearPassword = new Button("Clear Password");
        Button btnSaveFileLocation = new Button("Set Save File Location");
        Button btnSetDefaultScenarioDirectory = new Button("Set Default Scenario Directory");
        Button btnSetDefaultVideoDirectory = new Button("Set Default Video Directory");
        btnSetPassword.setOnAction(a -> setPassword());
        btnClearPassword.setOnAction(a -> clearPassword());
        btnSaveFileLocation.setOnAction(a -> setSaveFileLocation());
        btnSetDefaultScenarioDirectory.setOnAction(a -> setDefaultScenarioDirectory());
        btnSetDefaultVideoDirectory.setOnAction(a -> setDefaultVideoDirectory());

        minVideoWidth.setOnKeyReleased(e -> saveMinimumVideoDimensions());
        minVideoHeight.setOnKeyReleased(e -> saveMinimumVideoDimensions());

        GridPane formControls = new FormBuilder()
                .addLabel("edus2j Configuration Settings")
                .addControl("Minimum Video Width", minVideoWidth)
                .addControl("Minimum Video Height", minVideoHeight)
                .addControl("Set Password", btnSetPassword)
                .addControl("Clear Password", btnClearPassword)
                .addControl("Default Scenario Directory", btnSetDefaultScenarioDirectory)
                .addControl("Default Video Directory", btnSetDefaultVideoDirectory)
                .addControl("Save File Location", btnSaveFileLocation)
                .build();

        this.getChildren().add(formControls);
    }

    private void setDefaultVideoDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File defaultVideoDirectory = directoryChooser.showDialog(this.stage);
        if (defaultVideoDirectory != null) {
            configuration.setDefaultVideoDirectory(defaultVideoDirectory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Default video directory updated.");
            alert.setHeaderText("Default Video Directory Successfully Updated");
            alert.showAndWait();
        }
    }

    private void setDefaultScenarioDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File defaultScenarioDirectory = directoryChooser.showDialog(this.stage);
        if (defaultScenarioDirectory != null) {
            configuration.setDefaultScenarioDirectory(defaultScenarioDirectory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Default scenario directory updated.");
            alert.setHeaderText("Default Scenario Directory Successfully Updated");
            alert.showAndWait();
        }
    }

    private void setSaveFileLocation() {
        FileChooser fileChooser = new FileChooser();
        File saveFile = fileChooser.showSaveDialog(this.stage);
        if (saveFile != null) {
            configuration.setSaveFileLocation(saveFile.getAbsolutePath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Save file location updated.");
            alert.setHeaderText("Save File Location Successfully Updated");
            alert.showAndWait();
        }
    }

    private void setPassword() {
        PasswordInputDialog passwordInputDialog = new PasswordInputDialog("Set Password", "Enter new password used to secure scan settings");
        Optional<String> password = passwordInputDialog.showAndWait();
        if (password.isPresent()) {
            PasswordInputDialog passwordConfirmationDialog = new PasswordInputDialog("Confirm Password", "Please confirm the password you entered");
            Optional<String> passwordConfirmation = passwordConfirmationDialog.showAndWait();
            if (passwordConfirmation.isPresent() && password.get().equals(passwordConfirmation.get())) {
                authenticationFacade.setPassword(password.get());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password successfully set.");
                alert.setHeaderText("Success");
                alert.showAndWait();
            } else if (passwordConfirmation.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Password and password confirmation didn't match!");
                alert.setHeaderText("Failure");
                alert.showAndWait();
            }
        }
    }

    private void clearPassword() {
        authenticationFacade.clearPassword();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Password has been cleared.");
        alert.showAndWait();
    }

    private void saveMinimumVideoDimensions() {
        if (!validateMinVideoWidth()) {
            return;
        }

        if (!validateMinVideoHeight()) {
            return;
        }

        saveMinVideoWidth();
        saveMinVideoHeight();
    }

    private boolean validateMinVideoWidth() {
        return validateMinimumVideoDimensions(minVideoWidth.getText());
    }

    private boolean validateMinVideoHeight() {
        return validateMinimumVideoDimensions(minVideoHeight.getText());
    }

    private boolean validateMinimumVideoDimensions(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Minimum video width/height can be %s at most: %s is too large", Integer.MAX_VALUE, value));
            alert.showAndWait();
            return false;
        }
    }

    private void saveMinVideoWidth() {
        String rawText = minVideoWidth.getText();
        if (StringUtils.isEmpty(rawText)) {
            configuration.setMinimumVideoWidth(0);
            return;
        }

        configuration.setMinimumVideoWidth(Integer.parseInt(rawText));
    }

    private void saveMinVideoHeight() {
        String rawText = minVideoHeight.getText();
        if (StringUtils.isEmpty(rawText)) {
            configuration.setMinimumVideoHeight(0);
            return;
        }

        configuration.setMinimumVideoHeight(Integer.parseInt(rawText));
    }

}
