package edus2.adapter.ui;

import edus2.application.AuthenticationFacade;
import edus2.application.EDUS2View;
import edus2.domain.EDUS2Configuration;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class ConfigurationWindow extends VBox {
    private final TextField minVideoWidth;
    private final TextField minVideoHeight;
    private Stage stage;
    private EDUS2Configuration configuration;
    private AuthenticationFacade authenticationFacade;

    public ConfigurationWindow(EDUS2Configuration configuration, AuthenticationFacade authenticationFacade) {
        super(10);
        this.authenticationFacade = authenticationFacade;
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

        this.getChildren().addAll(new HBox(new Text("Minimum Video Width"), minVideoWidth));
        this.getChildren().addAll(new HBox(new Text("Minimum Video Height"), minVideoHeight));
        Button btnSaveChanges = new Button("Save Changes");
        Button btnSetPassword = new Button("Set Password");
        Button btnClearPassword = new Button("Clear Password");
        Button btnScanFileLocation = new Button("Set Scan File Location");
        btnSetPassword.setOnAction(a -> setPassword());
        btnClearPassword.setOnAction(a -> clearPassword());
        btnScanFileLocation.setOnAction(a -> setScanFileLocation());
        btnSaveChanges.setOnAction(a -> saveChanges());
        HBox saveBox = new HBox(btnSaveChanges);
        saveBox.setAlignment(Pos.CENTER);
        HBox auxiliaryControls = new HBox(10, btnSetPassword, btnClearPassword, btnScanFileLocation);
        auxiliaryControls.setAlignment(Pos.CENTER);
        this.getChildren().addAll(saveBox, auxiliaryControls);
    }

    private void setScanFileLocation() {
        FileChooser fileChooser = new FileChooser();
        File scanFile = fileChooser.showOpenDialog(this.stage);
        configuration.setScanFileLocation(scanFile.getAbsolutePath());
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Scan file location updated.");
        alert.setHeaderText("Scan File Location Successfully Updated");
        alert.showAndWait();
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

    private void saveChanges() {
        if (!validateMinVideoWidth()) {
            return;
        }

        if (!validateMinVideoHeight()) {
            return;
        }

        processMinVideoWidth();
        processMinVideoHeight();
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

    private void processMinVideoWidth() {
        String rawText = minVideoWidth.getText();
        if (StringUtils.isEmpty(rawText)) {
            return;
        }

        configuration.setMinimumVideoWidth(Integer.parseInt(rawText));
    }

    private void processMinVideoHeight() {
        String rawText = minVideoHeight.getText();
        if (StringUtils.isEmpty(rawText)) {
            return;
        }

        configuration.setMinimumVideoHeight(Integer.parseInt(rawText));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(EDUS2View.getThumbnailImage());
    }
}
