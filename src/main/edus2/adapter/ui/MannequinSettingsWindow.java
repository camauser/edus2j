package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class MannequinSettingsWindow extends VBox{
    private MannequinsWindow mannequinDisplay;

    public MannequinSettingsWindow(MannequinFacade mannequinFacade) {
        // Just set up a settings window, which is then shown on-screen
        super(10);
        mannequinDisplay = new MannequinsWindow(mannequinFacade);
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update Scan Tags");
        Button btnChangeName = new Button("Update Name");
        Button btnDelete = new Button("Delete");

        // When add is clicked, run through the process of adding a new scan
        btnAdd.setOnAction(event -> {
            MannequinCreationWindow mannequinSettingsWindow = new MannequinCreationWindow(mannequinFacade);
            Stage stage = new Stage();
            Scene scene = new Scene(mannequinSettingsWindow);
            stage.setScene(scene);
            stage.showAndWait();
            mannequinDisplay.refreshTableItems();
        });

        btnUpdate.setOnAction(event -> {
            Mannequin selected = mannequinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            MannequinCreationWindow mannequinSettingsWindow = new MannequinCreationWindow(mannequinFacade);
            mannequinSettingsWindow.bindMannequin(selected.getName());
            Stage stage = new Stage();
            Scene scene = new Scene(mannequinSettingsWindow);
            stage.setScene(scene);
            stage.showAndWait();
            mannequinDisplay.refreshTableItems();
        });

        btnChangeName.setOnAction(event -> {
            Mannequin selected = mannequinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please enter the new mannequin name:");
            Optional<String> newNameOptional = dialog.showAndWait();
            if (newNameOptional.isPresent()) {
                try {
                    mannequinFacade.rename(selected.getName(), newNameOptional.get());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Mannequin '%s' has been renamed to '%s'", selected.getName(), newNameOptional.get()));
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Error changing name: %s", e.getMessage()));
                    alert.showAndWait();
                }
                mannequinDisplay.refreshTableItems();
            }
        });

        // Delete the selected scan when the delete button is clicked.
        btnDelete.setOnAction(event -> {
            Mannequin selected = mannequinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            mannequinFacade.remove(selected);
            mannequinDisplay.refreshTableItems();
        });

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnUpdate, btnChangeName, btnDelete);

        mannequinDisplay.setAlignment(Pos.CENTER);
        this.getChildren().addAll(mannequinDisplay, scanSettingButtonsBox);

    }

}
