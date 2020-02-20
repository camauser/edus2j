package edus2.adapter.ui;/*
 * Copyright 2016 Paul Kulyk, Paul Olszynski, Cameron Auser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import edus2.adapter.repository.file.FileScanImportExportRepository;
import edus2.adapter.ui.handler.*;
import edus2.application.AuthenticationFacade;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Purpose: Display a settings window for EDUS2.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class ScanSettingsWindow extends VBox {
    private EDUS2IconStage stage;
    private ScansWindow scanList;
    private ScanFacade scanFacade;
    private FileScanImportExportRepository importExportRepository;
    private EDUS2Configuration configuration;

    public ScanSettingsWindow(ScanFacade scanFacade, AuthenticationFacade authenticationFacade, EDUS2Configuration configuration, EDUS2IconStage stage) {
        // Just set up a settings window, which is then shown on-screen
        super(10);
        this.scanFacade = scanFacade;
        this.importExportRepository = new FileScanImportExportRepository(scanFacade);
        this.stage = stage;
        this.configuration = configuration;
        scanList = new ScansWindow(scanFacade);

        HBox scanSettingButtonsBox = setupControlButtons();

        Button btnConfigSettings = new Button("Configuration Settings");
        btnConfigSettings.setOnAction(e -> {
            EDUS2IconStage configurationStage = new EDUS2IconStage();
            ConfigurationWindow configurationWindow = new ConfigurationWindow(configuration, authenticationFacade, configurationStage);
            Scene configurationScene = new Scene(configurationWindow);
            configurationStage.setScene(configurationScene);
            configurationStage.showAndWait();
            // needed to keep scan list up-to-date if scan file is changed
            scanList.refreshTableItems();
        });

        HBox configurationButtonBox = new HBox();
        configurationButtonBox.setAlignment(Pos.CENTER);
        configurationButtonBox.getChildren().add(btnConfigSettings);
        this.getChildren().addAll(scanList, scanSettingButtonsBox, configurationButtonBox);

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

        AddScanHandler addScanHandler = new AddScanHandler(scanFacade);
        BulkAddScanHandler bulkAddScanHandler = new BulkAddScanHandler(scanFacade);
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

    private void registerHandler(Button button, BaseHandler handler) {
        button.setOnAction(event -> {
            handler.handle(scanList.getSelectedItems(), stage);
            scanList.refreshTableItems();
        });
    }

}
