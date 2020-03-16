package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.repository.file.FileScanImportExportRepository;
import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.ScansWindow;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.settings.*;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ScanSettingsWindowContents extends SceneContents {
    private final ScanFacade scanFacade;
    private final EDUS2Configuration configuration;
    private final FileScanImportExportRepository importExportRepository;
    private final EDUS2IconStage stage;
    private final ScansWindow scanList;
    private final ConfigurationWindowContents configurationWindowContents;

    @Inject
    public ScanSettingsWindowContents(SceneBuilder sceneBuilder, ScanFacade scanFacade, EDUS2Configuration configuration,
                                      FileScanImportExportRepository importExportRepository,
                                      EDUS2IconStage stage, ScansWindow scansWindow,
                                      ConfigurationWindowContents configurationWindowContents) {
        super(sceneBuilder);
        this.scanFacade = scanFacade;
        this.configuration = configuration;
        this.importExportRepository = importExportRepository;
        this.stage = stage;
        this.scanList = scansWindow;
        this.configurationWindowContents = configurationWindowContents;
    }

    @Override
    protected Parent buildSceneContents() {
        VBox settingsWindow = new VBox(10);
        HBox scanSettingButtonsBox = setupControlButtons();

        Button btnConfigSettings = new Button("Configuration Settings");
        btnConfigSettings.setOnAction(e -> {
            EDUS2IconStage configurationStage = new EDUS2IconStage();
            Scene configurationScene = configurationWindowContents.getScene();
            configurationStage.setScene(configurationScene);
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

        AddScanHandler addScanHandler = new AddScanHandler(scanFacade, configuration);
        BulkAddScanHandler bulkAddScanHandler = new BulkAddScanHandler(scanFacade, configuration);
        EditManikinLocationHandler editManikinLocationHandler = new EditManikinLocationHandler(scanFacade);
        EditScanFileHandler editScanFileHandler = new EditScanFileHandler(scanFacade);
        DeleteScanHandler deleteScanHandler = new DeleteScanHandler(scanFacade);
        DeleteAllScanHandler deleteAllScanHandler = new DeleteAllScanHandler(scanFacade);
        LoadScenarioHandler loadScenarioHandler = new LoadScenarioHandler(scanFacade, configuration, importExportRepository);
        SaveScenarioHandler saveScenarioHandler = new SaveScenarioHandler(scanFacade, configuration, importExportRepository);

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
