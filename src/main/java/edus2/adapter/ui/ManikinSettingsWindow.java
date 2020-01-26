package edus2.adapter.ui;

import edus2.adapter.repository.file.FileManikinImportExportRepository;
import edus2.application.ManikinFacade;
import edus2.domain.InvalidManikinNameException;
import edus2.domain.Manikin;
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

public class ManikinSettingsWindow extends VBox {
    private ManikinsWindow manikinDisplay;
    private FileManikinImportExportRepository importExportRepository;
    private EDUS2IconStage stage;

    public ManikinSettingsWindow(ManikinFacade manikinFacade, EDUS2IconStage stage) {
        super(10);
        manikinDisplay = new ManikinsWindow(manikinFacade);
        importExportRepository = new FileManikinImportExportRepository(manikinFacade);
        this.stage = stage;
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update Scan Tags");
        Button btnChangeName = new Button("Update Name");
        Button btnDelete = new Button("Delete");
        Button btnImport = new Button("Import");
        Button btnExport = new Button("Export");

        btnAdd.setOnAction(event -> {
            ManikinCreateWindow manikinCreateWindow = new ManikinCreateWindow(manikinFacade);
            EDUS2IconStage addStage = new EDUS2IconStage();
            addStage.widthProperty().addListener((ob, oldVal, newVal) -> manikinCreateWindow.handleWindowResize(newVal.intValue()));
            Scene scene = new Scene(manikinCreateWindow);
            addStage.setScene(scene);
            addStage.showAndWait();
            manikinDisplay.refreshTableItems();
        });

        btnUpdate.setOnAction(event -> {
            Manikin selected = manikinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }

            EDUS2IconStage updateStage = new EDUS2IconStage();
            ManikinUpdateWindow manikinUpdateWindow = new ManikinUpdateWindow(manikinFacade);
            manikinUpdateWindow.bindManikin(manikinFacade.getManikin(selected.getName()).orElseThrow(() -> new InvalidManikinNameException(String.format("Manikin %s does not exist!", selected.getName()))));
            updateStage.widthProperty().addListener((ob, oldVal, newVal) -> manikinUpdateWindow.handleWindowResize(newVal.intValue()));
            Scene scene = new Scene(manikinUpdateWindow);
            updateStage.setScene(scene);
            updateStage.showAndWait();
            manikinDisplay.refreshTableItems();
        });

        btnChangeName.setOnAction(event -> {
            Manikin selected = manikinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setContentText("Please enter the new manikin name:");
            Optional<String> newNameOptional = dialog.showAndWait();
            if (newNameOptional.isPresent()) {
                try {
                    manikinFacade.rename(selected.getName(), newNameOptional.get());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Manikin '%s' has been renamed to '%s'", selected.getName(), newNameOptional.get()));
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Error changing name: %s", e.getMessage()));
                    alert.showAndWait();
                }
                manikinDisplay.refreshTableItems();
            }
        });

        btnDelete.setOnAction(event -> {
            Manikin selected = manikinDisplay.getSelectedItem();
            if (selected == null) {
                return;
            }
            manikinFacade.remove(selected);
            manikinDisplay.refreshTableItems();
        });

        btnImport.setOnAction(event -> importManikins());

        btnExport.setOnAction(event -> exportManikins());

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnUpdate, btnChangeName, btnDelete, btnImport, btnExport);

        manikinDisplay.setAlignment(Pos.CENTER);
        this.getChildren().addAll(manikinDisplay, scanSettingButtonsBox);

    }

    private void importManikins() {
        FileChooser browser = new FileChooser();
        File scanFile = browser.showOpenDialog(stage);
        if (scanFile != null) {
            try {
                importExportRepository.importManikinsFromFile(scanFile);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Encountered error while importing manikins: %s", e.getMessage()));
                alert.showAndWait();
            }
            manikinDisplay.refreshTableItems();
        }
    }

    private void exportManikins() {
        FileChooser browser = new FileChooser();
        File selected = browser.showSaveDialog(stage);
        if (selected != null) {
            try {
                importExportRepository.exportManikinsToFile(selected);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Manikins exported successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Encountered error while exporting manikins: %s", e.getMessage()));
                alert.showAndWait();
            }
        }
    }

}
