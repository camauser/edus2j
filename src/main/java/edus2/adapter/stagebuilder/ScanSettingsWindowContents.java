package edus2.adapter.stagebuilder;

import com.google.inject.Inject;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ScansWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.settings.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ScanSettingsWindowContents extends StageBuilder {
    private final EDUS2IconStage stage;
    private final ScansWindow scanList;
    private final ConfigurationWindowContents configurationWindowContents;
    private final AddScanHandler addScanHandler;
    private final BulkAddScanHandler bulkAddScanHandler;
    private final EditManikinLocationHandler editManikinLocationHandler;
    private final EditScanFileHandler editScanFileHandler;
    private final DeleteScanHandler deleteScanHandler;
    private final DeleteAllScanHandler deleteAllScanHandler;
    private final LoadScenarioHandler loadScenarioHandler;
    private final SaveScenarioHandler saveScenarioHandler;

    @Inject
    public ScanSettingsWindowContents(SceneBuilder sceneBuilder,
                                      EDUS2IconStage stage, ScansWindow scansWindow,
                                      ConfigurationWindowContents configurationWindowContents,
                                      AddScanHandler addScanHandler, BulkAddScanHandler bulkAddScanHandler,
                                      EditManikinLocationHandler editManikinLocationHandler,
                                      EditScanFileHandler editScanFileHandler, DeleteScanHandler deleteScanHandler,
                                      DeleteAllScanHandler deleteAllScanHandler, LoadScenarioHandler loadScenarioHandler,
                                      SaveScenarioHandler saveScenarioHandler) {
        super(sceneBuilder);
        this.stage = stage;
        this.scanList = scansWindow;
        this.configurationWindowContents = configurationWindowContents;
        this.addScanHandler = addScanHandler;
        this.bulkAddScanHandler = bulkAddScanHandler;
        this.editManikinLocationHandler = editManikinLocationHandler;
        this.editScanFileHandler = editScanFileHandler;
        this.deleteScanHandler = deleteScanHandler;
        this.deleteAllScanHandler = deleteAllScanHandler;
        this.loadScenarioHandler = loadScenarioHandler;
        this.saveScenarioHandler = saveScenarioHandler;
    }

    @Override
    protected Parent buildSceneContents() {
        VBox settingsWindow = new VBox(10);
        HBox scanSettingButtonsBox = setupControlButtons();

        Button btnConfigSettings = new Button("Configuration Settings");
        btnConfigSettings.setOnAction(e -> {
            EDUS2IconStage configurationStage = configurationWindowContents.build();
            configurationStage.showAndWait();
            // needed to keep scan list up-to-date if scan file is changed
            scanList.refreshTableItems();
        });

        HBox configurationButtonBox = new HBox();
        configurationButtonBox.setAlignment(Pos.CENTER);
        configurationButtonBox.getChildren().add(btnConfigSettings);
        settingsWindow.getChildren().addAll(scanList, scanSettingButtonsBox, configurationButtonBox);
        return settingsWindow;
    }

    @Override
    public String getTitle() {
        return "Scan Settings";
    }

    private HBox setupControlButtons() {
        HBox scanSettingButtonsBox = new HBox();
        Button btnAdd = new Button("Add Video");
        Button btnBulkAdd = new Button("Bulk Add Videos");
        Button btnEditManikinLocation = new Button("Edit Manikin Location");
        Button btnEditFile = new Button("Edit Video Path");
        Button btnDelete = new Button("Delete Video");
        Button btnDeleteAll = new Button("Delete All Videos");
        Button btnLoadScenario = new Button("Load Scenario");
        Button btnSaveScenario = new Button("Save Scenario");

        registerHandler(btnAdd, addScanHandler);
        registerHandler(btnBulkAdd, bulkAddScanHandler);
        registerHandler(btnEditManikinLocation, editManikinLocationHandler);
        registerHandler(btnEditFile, editScanFileHandler);
        registerHandler(btnDelete, deleteScanHandler);
        registerHandler(btnDeleteAll, deleteAllScanHandler);
        registerHandler(btnLoadScenario, loadScenarioHandler);
        registerHandler(btnSaveScenario, saveScenarioHandler);

        scanSettingButtonsBox.setAlignment(Pos.CENTER);
        scanSettingButtonsBox.getChildren().addAll(btnAdd, btnBulkAdd, btnEditManikinLocation, btnEditFile, btnDelete, btnDeleteAll, btnLoadScenario, btnSaveScenario);

        return scanSettingButtonsBox;
    }

    private void registerHandler(Button button, SettingsHandler handler) {
        button.setOnAction(event -> {
            handler.handle(scanList.getSelectedItems(), stage);
            scanList.refreshTableItems();
        });
    }
}
