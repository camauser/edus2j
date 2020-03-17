package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.repository.file.FileManikinImportExportRepository;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ManikinsWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.InvalidManikinNameException;
import edus2.domain.Manikin;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class ManikinSettingsWindowContents extends SceneContents {

    private final ManikinFacade manikinFacade;
    private final EDUS2IconStage stage;
    private final ManikinsWindow manikinsWindow;
    private final FileManikinImportExportRepository importExportRepository;
    private final ManikinCreateWindowContents manikinCreateWindowContents;
    private final ManikinUpdateWindowContents manikinUpdateWindowContents;

    @Inject
    public ManikinSettingsWindowContents(SceneBuilder sceneBuilder, ManikinFacade manikinFacade, EDUS2IconStage stage,
                                         ManikinsWindow manikinsWindow,
                                         FileManikinImportExportRepository importExportRepository,
                                         ManikinCreateWindowContents manikinCreateWindowContents,
                                         ManikinUpdateWindowContents manikinUpdateWindowContents) {
        super(sceneBuilder);
        this.manikinFacade = manikinFacade;
        this.stage = stage;
        this.manikinsWindow = manikinsWindow;
        this.importExportRepository = importExportRepository;
        this.manikinCreateWindowContents = manikinCreateWindowContents;
        this.manikinUpdateWindowContents = manikinUpdateWindowContents;
    }

    @Override
    protected Parent buildSceneContents() {
        VBox manikinSettingsBox = new VBox(10);
        HBox scanSettingButtonsBox = new HBox();

        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update Scan Tags");
        Button btnChangeName = new Button("Update Name");
        Button btnDelete = new Button("Delete");
        Button btnImport = new Button("Import Manikins");
        Button btnExport = new Button("Export Manikins");

        btnAdd.setOnAction(event -> {
            EDUS2IconStage addStage = new EDUS2IconStage();
            addStage.widthProperty().addListener((ob, oldVal, newVal) -> manikinCreateWindowContents.handleWindowResize(newVal.intValue()));
            Scene scene = manikinCreateWindowContents.getScene();
            addStage.setScene(scene);
            addStage.showAndWait();
            manikinsWindow.refreshTableItems();
        });

        btnUpdate.setOnAction(event -> {
            Manikin selected = manikinsWindow.getSelectedItem();
            if (selected == null) {
                return;
            }

            EDUS2IconStage updateStage = new EDUS2IconStage();

            Scene scene = manikinUpdateWindowContents.getScene();
            manikinUpdateWindowContents.bindManikin(manikinFacade.getManikin(selected.getName()).orElseThrow(() -> new InvalidManikinNameException(String.format("Manikin %s does not exist!", selected.getName()))));
            updateStage.widthProperty().addListener((ob, oldVal, newVal) -> manikinUpdateWindowContents.handleWindowResize(newVal.intValue()));
            updateStage.setScene(scene);
            updateStage.showAndWait();
            manikinsWindow.refreshTableItems();
        });

        btnChangeName.setOnAction(event -> {
            Manikin selected = manikinsWindow.getSelectedItem();
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
                manikinsWindow.refreshTableItems();
            }
        });

        btnDelete.setOnAction(event -> {
            Manikin selected = manikinsWindow.getSelectedItem();
            if (selected == null) {
                return;
            }

            Alert deleteConfirmation = new Alert(Alert.AlertType.INFORMATION, String.format("Click OK to confirm deletion of %s.", selected.getName()));
            deleteConfirmation.setHeaderText("Confirm Deletion");
            deleteConfirmation.setTitle("Confirm Deletion");
            Optional<ButtonType> response = deleteConfirmation.showAndWait();
            boolean deleteConfirmed = response.isPresent();
            if (deleteConfirmed) {
                manikinFacade.remove(selected);
                manikinsWindow.refreshTableItems();
            }
        });

        btnImport.setOnAction(event -> importManikins());

        btnExport.setOnAction(event -> exportManikins());

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnUpdate, btnChangeName, btnDelete, btnImport, btnExport);

        manikinsWindow.setAlignment(Pos.CENTER);
        manikinSettingsBox.getChildren().addAll(manikinsWindow, scanSettingButtonsBox);
        return manikinSettingsBox;
    }

    private void importManikins() {
        File scanFile = promptForKinFileOpen();
        if (scanFile != null) {
            try {
                importExportRepository.importManikinsFromFile(scanFile);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Encountered error while importing manikins: %s", e.getMessage()));
                alert.showAndWait();
            }
            manikinsWindow.refreshTableItems();
        }
    }

    private void exportManikins() {
        File selected = promptForKinFileSave();
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

    protected File promptForKinFileSave() {
        FileChooser browser = new FileChooser();
        browser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Manikin file format (*.kin)", "*.kin"));
        return browser.showSaveDialog(stage);
    }

    protected File promptForKinFileOpen() {
        FileChooser browser = new FileChooser();
        browser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Manikin file format (*.kin)", "*.kin"));
        return browser.showOpenDialog(stage);
    }
}
