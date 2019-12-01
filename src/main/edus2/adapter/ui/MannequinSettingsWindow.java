package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MannequinSettingsWindow extends VBox{
    private MannequinsWindow mannequinDisplay;

    public MannequinSettingsWindow(MannequinFacade mannequinFacade) {
        // Just set up a settings window, which is then shown on-screen
        super(10);
        mannequinDisplay = new MannequinsWindow(mannequinFacade);
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnDelete = new Button("Delete");

        // When add is clicked, run through the process of adding a new scan
        btnAdd.setOnAction(event -> {
            MannequinCreationWindow mannequinSettingsWindow = new MannequinCreationWindow(mannequinFacade);
            Stage stage = new Stage();
            Scene scene = new Scene(mannequinSettingsWindow);
            stage.setScene(scene);
            stage.setWidth(mannequinSettingsWindow.getWidth());
            stage.show();
        });

        // Delete the selected scan when the delete button is clicked.
        btnDelete.setOnAction(event -> {
            Mannequin selected = mannequinDisplay.getSelectedItem();
            mannequinFacade.remove(selected);
            mannequinDisplay.refreshTableItems();
        });

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnDelete);

        this.getChildren().addAll(mannequinDisplay, scanSettingButtonsBox);

    }

}
