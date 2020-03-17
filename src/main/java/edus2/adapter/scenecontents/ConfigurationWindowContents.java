package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.PasswordInputDialog;
import edus2.adapter.ui.builder.FormBuilder;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.AuthenticationFacade;
import edus2.domain.EDUS2Configuration;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ConfigurationWindowContents extends SceneContents {
    private final EDUS2Configuration configuration;
    private final AuthenticationFacade authenticationFacade;
    private final EDUS2IconStage stage;
    private final TextField minVideoWidth;
    private final TextField minVideoHeight;

    @Inject
    public ConfigurationWindowContents(SceneBuilder sceneBuilder, EDUS2Configuration configuration, AuthenticationFacade authenticationFacade, EDUS2IconStage stage) {
        super(sceneBuilder);
        this.configuration = configuration;
        this.authenticationFacade = authenticationFacade;
        this.stage = stage;
        minVideoWidth = new TextField();
        minVideoHeight = new TextField();
    }

    @Override
    protected Parent buildSceneContents() {
        HBox configurationBox = new HBox(10);
        UnaryOperator<TextFormatter.Change> integerFilter = entry -> {
            String text = entry.getText();
            if (text.matches("^\\d*$")) {
                return entry;
            }

            return null;
        };

        minVideoWidth.setTextFormatter(new TextFormatter<>(integerFilter));
        minVideoHeight.setTextFormatter(new TextFormatter<>(integerFilter));

        minVideoWidth.setText(configuration.getMinimumVideoWidth().map(Object::toString).orElse(""));
        minVideoHeight.setText(configuration.getMinimumVideoHeight().map(Object::toString).orElse(""));

        Button btnSetPassword = new Button("Set Password");
        Button btnClearPassword = new Button("Clear Password");
        Button btnSaveFileLocation = new Button("Set Save File Location");
        Button btnSetDefaultScenarioDirectory = new Button("Set Default Scenario Directory");
        Button btnSetDefaultVideoDirectory = new Button("Set Default Video Directory");
        CheckBox chkEnableDarkMode = new CheckBox("Enable");
        btnSetPassword.setOnAction(a -> setPassword());
        btnClearPassword.setOnAction(a -> clearPassword());

        btnSaveFileLocation.setOnAction(a -> setSaveFileLocation());
        btnSetDefaultScenarioDirectory.setOnAction(a -> setDefaultScenarioDirectory());
        btnSetDefaultVideoDirectory.setOnAction(a -> setDefaultVideoDirectory());

        chkEnableDarkMode.setOnAction(a -> setDarkMode(chkEnableDarkMode));
        chkEnableDarkMode.setSelected(configuration.darkModeEnabledProperty().get());

        minVideoWidth.setOnKeyReleased(e -> saveMinVideoWidth());
        minVideoHeight.setOnKeyReleased(e -> saveMinVideoHeight());

        GridPane formControls = new FormBuilder()
                .addLabel("edus2j Configuration Settings")
                .addControl("Minimum Video Width (px)", minVideoWidth)
                .addControl("Minimum Video Height (px)", minVideoHeight)
                .addControl("Set Password", btnSetPassword)
                .addControl("Clear Password", btnClearPassword)
                .addControl("Dark Mode", chkEnableDarkMode)
                .addControl("Default Scenario Directory", btnSetDefaultScenarioDirectory)
                .addControl("Default Video Directory", btnSetDefaultVideoDirectory)
                .addControl("Save File Location", btnSaveFileLocation)
                .build();

        configurationBox.getChildren().add(formControls);
        return configurationBox;
    }

    private void setDarkMode(CheckBox darkModeCheckbox) {
        configuration.setDarkModeEnabled(darkModeCheckbox.isSelected());
    }

    private void setDefaultVideoDirectory() {
        File defaultVideoDirectory = promptForDirectory();
        if (defaultVideoDirectory != null) {
            configuration.setDefaultVideoDirectory(defaultVideoDirectory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Default video directory updated.");
            alert.setHeaderText("Default Video Directory Successfully Updated");
            alert.showAndWait();
        }
    }

    private void setDefaultScenarioDirectory() {
        File defaultScenarioDirectory = promptForDirectory();
        if (defaultScenarioDirectory != null) {
            configuration.setDefaultScenarioDirectory(defaultScenarioDirectory);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Default scenario directory updated.");
            alert.setHeaderText("Default Scenario Directory Successfully Updated");
            alert.showAndWait();
        }
    }

    private void setSaveFileLocation() {
        File saveFile = promptForFile();
        if (saveFile != null) {
            configuration.setSaveFileLocation(saveFile.getAbsolutePath());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Save file location updated.");
            alert.setHeaderText("Save File Location Successfully Updated");
            alert.showAndWait();
        }
    }

    protected File promptForDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(this.stage);
    }

    protected File promptForFile() {
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showSaveDialog(this.stage);
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

    private boolean validVideoDimension(String attempt) {
        if (StringUtils.isEmpty(attempt)) {
            return false;
        }

        try {
            Integer.parseInt(attempt);
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Minimum video width/height can be %s at most: %s is too large", Integer.MAX_VALUE, attempt));
            alert.showAndWait();
            return false;
        }
    }

    private void saveMinVideoWidth() {
        String rawText = minVideoWidth.getText();
        if (validVideoDimension(rawText)) {
            configuration.setMinimumVideoWidth(Integer.parseInt(rawText));
        }

    }

    private void saveMinVideoHeight() {
        String rawText = minVideoHeight.getText();
        if (validVideoDimension(rawText)) {
            configuration.setMinimumVideoHeight(Integer.parseInt(rawText));
        }

    }
}
