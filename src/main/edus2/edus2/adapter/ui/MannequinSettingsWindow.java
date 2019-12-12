package edus2.adapter.ui;

import edus2.adapter.repository.file.FileMannequinImportExportRepository;
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
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class MannequinSettingsWindow extends VBox{
    private MannequinsWindow mannequinDisplay;
    private FileMannequinImportExportRepository importExportRepository;
    private EDUS2IconStage stage;

    public MannequinSettingsWindow(MannequinFacade mannequinFacade) {
        super(10);
        mannequinDisplay = new MannequinsWindow(mannequinFacade);
        importExportRepository = new FileMannequinImportExportRepository(mannequinFacade);
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update Scan Tags");
        Button btnChangeName = new Button("Update Name");
        Button btnDelete = new Button("Delete");
        Button btnImport = new Button("Import");
        Button btnExport = new Button("Export");

        btnAdd.setOnAction(event -> {
            MannequinCreateWindow mannequinCreateWindow = new MannequinCreateWindow(mannequinFacade);
            EDUS2IconStage stage = new EDUS2IconStage();
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
            EDUS2IconStage stage = new EDUS2IconStage();
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

        btnImport.setOnAction(event -> importMannequins());

        btnExport.setOnAction(event -> exportMannequins());

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnUpdate, btnChangeName, btnDelete, btnImport, btnExport);

        mannequinDisplay.setAlignment(Pos.CENTER);
        this.getChildren().addAll(mannequinDisplay, scanSettingButtonsBox);

    }

    public void setStage(EDUS2IconStage stage) {
        this.stage = stage;
    }

    private void importMannequins() {
        FileChooser browser = new FileChooser();
        File scanFile = browser.showOpenDialog(stage);
        if (scanFile != null) {
            try {
                importExportRepository.importMannequinsFromFile(scanFile);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Encountered error while importing mannequins: %s", e.getMessage()));
                alert.showAndWait();
            }
            mannequinDisplay.refreshTableItems();
        }
    }

    private void exportMannequins() {
        FileChooser browser = new FileChooser();
        File selected = browser.showSaveDialog(stage);
        if (selected != null) {
            try {
                importExportRepository.exportMannequinsToFile(selected);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Mannequins exported successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Encountered error while exporting mannequins: %s", e.getMessage()));
                alert.showAndWait();
            }
        }
    }

}
