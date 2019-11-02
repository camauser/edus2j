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

import edus2.adapter.logging.LoggerSingleton;
import edus2.application.EDUS2View;
import edus2.application.ScanFacade;
import edus2.application.exception.EmptyScanIdException;
import edus2.application.exception.ScanAlreadyExistsException;
import edus2.domain.Scan;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Purpose: Display a settings window for EDUS2.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class SettingsWindow extends VBox {
    private Stage stage;
    private ScansWindow scanList;
    private ScanFacade scanFacade;

    /**
     * Constructor for the SettingsWindow class.
     *
     * @param scanFacade - the Scans to pass into the ScansWindow constructor.
     */
    public SettingsWindow(ScanFacade scanFacade) {
        // Just set up a settings window, which is then shown on-screen
        super(10);
        this.scanFacade = scanFacade;
        scanList = new ScansWindow(scanFacade);
        HBox buttons = new HBox();

        Button btnAdd = new Button("Add");
        Button btnBulkAdd = new Button("Bulk Add");
        Button btnDelete = new Button("Delete");
        Button btnDeleteAll = new Button("Delete All");
        Button btnImport = new Button("Import from File");
        Button btnExport = new Button("Export to File");

        // When add is clicked, run through the process of adding a new scan
        btnAdd.setOnAction(event -> {
            FileChooser browser = new FileChooser();
            File selected = browser.showOpenDialog(stage);
            if (selected != null) {
                promptForScanIdAndSaveScan(selected);
            }
        });

        // When bulk add is clicked, the user can select multiple files to add.
        // Then we run through each selected file and set ID's for all of them.
        btnBulkAdd.setOnAction(event -> {
            FileChooser browser = new FileChooser();
            List<File> selected = browser.showOpenMultipleDialog(stage);
            if (selected != null) {
                for (File current : selected) {
                    promptForScanIdAndSaveScan(current);
                }
            }
        });

        // Delete the selected scan when the delete button is clicked.
        btnDelete.setOnAction(event -> {
            Scan selected = scanList.getSelectedItem();
            scanFacade.removeScan(selected);
            scanList.refreshTableItems();
        });

        // Delete ALL the scans when the delete all button is clicked.
        btnDeleteAll.setOnAction(event -> {
            // Show the user a warning, to let them know they're going
            // to permanently remove all scans from the program by doing this
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("It is suggested to export all scans before doing this.\nAre you sure you want to remove ALL scans?\n");
            alert.setHeaderText("Proceed with removing all scans?");
            alert.setTitle("Proceed with removing all scans?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                // If OK was clicked, we'll nuke all the scans
                scanFacade.removeAllScans();
                scanList.refreshTableItems();
            }
        });

        // Import scans from a file
        btnImport.setOnAction(event -> importScans());

        // Export all the current scans to a file
        btnExport.setOnAction(event -> exportScans());

        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btnAdd, btnBulkAdd, btnDelete,
                btnDeleteAll, btnImport, btnExport);
        this.getChildren().addAll(scanList, buttons);
    }

    private void promptForScanIdAndSaveScan(File file) {
        boolean added = false;
        while (!added) {
            String convertedFilePath = EDUS2View.convertFilePath(file.getPath());
            ScanPromptResponse response = promptForScanId("Filename: " + file.getName() + "\nWhat would you like the scan ID to be?");
            if (response.isCancelled()) {
                break;
            }

            if (response.getResponse().isPresent()) {
                String scanId = response.getResponse().get();
                added = addScan(scanId, convertedFilePath);
            } else {
                Alert alert = new Alert(AlertType.ERROR, "You must enter a scan ID!");
                alert.showAndWait();
            }
        }
    }

    private ScanPromptResponse promptForScanId(String prompt) {
        AtomicBoolean added = new AtomicBoolean(true);
        TextInputDialog nameEntry = new TextInputDialog();
        nameEntry.setHeaderText("Enter Scan ID");
        nameEntry.setContentText(prompt);
        nameEntry.getDialogPane().lookupButton(ButtonType.CANCEL).addEventFilter(ActionEvent.ACTION, e -> added.set(false));
        Optional<String> result = nameEntry.showAndWait();

        if (!added.get()) {
            return ScanPromptResponse.ofCancelled();
        }

        return ScanPromptResponse.ofResponse(result.orElse(null));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(EDUS2View.getThumbnailImage());
    }

    private void importScans() {
        FileChooser browser = new FileChooser();
        File scanFile = browser.showOpenDialog(stage);
        if (scanFile != null) {
            try {
                BufferedReader input = new BufferedReader(new FileReader(scanFile));
                StringBuilder entireFile = new StringBuilder();
                String currentLine;
                while ((currentLine = input.readLine()) != null) {
                    entireFile.append(currentLine).append("\n");
                }
                input.close();
                scanFacade.importCSV(entireFile.toString());
            } catch (Exception e) {
                e.printStackTrace();
                LoggerSingleton.logErrorIfEnabled("Encountered error importing scans: " + e.getMessage());
            }
        }
    }

    /**
     * Purpose: Export all scans to a file of the user's choice.
     */
    private void exportScans() {
        // Show a file browser, so the user can select a file to save to.
        FileChooser browser = new FileChooser();
        File selected = browser.showSaveDialog(stage);
        if (selected != null) {
            try {
                PrintWriter output = new PrintWriter(selected);
                // Print out our header first
                output.println(EDUS2View.IMPORT_MESSAGE);
                output.print(scanFacade.toCSV());
                output.flush();
                LoggerSingleton.logInfoIfEnabled("Exported all scans to file \"" + selected.getName() + "\"");
                output.close();
                Alert alert = new Alert(AlertType.CONFIRMATION, "Scans exported successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                LoggerSingleton.logErrorIfEnabled("Error exporting all scans: " + e.getMessage());
            }
        }
    }

    private boolean addScan(String id, String path) {
        Scan toAdd = new Scan(id, path);
        try {
            scanFacade.addScan(toAdd);
            scanList.refreshTableItems();
            return true;
        } catch (EmptyScanIdException | ScanAlreadyExistsException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return false;
        }
    }
}
