package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.InvalidMannequinNameException;
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
        super(10);
        mannequinDisplay = new MannequinsWindow(mannequinFacade);
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update Scan Tags");
        Button btnChangeName = new Button("Update Name");
        Button btnDelete = new Button("Delete");

        btnAdd.setOnAction(event -> {
            MannequinCreateWindow mannequinCreateWindow = new MannequinCreateWindow(mannequinFacade);
            Stage stage = new Stage();
            Scene scene = new Scene(mannequinCreateWindow);
            stage.setScene(scene);
            stage.showAndWait();
            mannequinDisplay.refreshTableItems();
        });

        btnUpdate.setOnAction(event -> {
            Mannequin selected = mannequinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            MannequinUpdateWindow mannequinUpdateWindow = new MannequinUpdateWindow(mannequinFacade);
            mannequinUpdateWindow.bindMannequin(mannequinFacade.getMannequin(selected.getName()).orElseThrow(() -> new InvalidMannequinNameException(String.format("Mannequin %s does not exist!", selected.getName()))));
            Stage stage = new Stage();
            Scene scene = new Scene(mannequinUpdateWindow);
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
