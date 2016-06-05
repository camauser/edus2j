/*
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * Purpose: Display a settings window for EDUS2.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class SettingsWindow extends VBox
{
    public Stage stage;
    ScansWindow scanList;
    ArrayList<Scan> scans;

    /**
     * 
     * Constructor for the SettingsWindow class.
     * 
     * @param scans
     *            - the Scans to pass into the ScansWindow constructor.
     */
    public SettingsWindow(ArrayList<Scan> scans)
    {
        // Just set up a settings window, which is then shown on-screen
        super(10);
        this.scans = scans;
        scanList = new ScansWindow(scans);
        HBox buttons = new HBox();

        Button btnAdd = new Button("Add");
        Button btnBulkAdd = new Button("Bulk Add");
        Button btnDelete = new Button("Delete");
        Button btnDeleteAll = new Button("Delete All");
        Button btnImport = new Button("Import from File");
        Button btnExport = new Button("Export to File");

        // When add is clicked, run through the process of adding a new scan
        btnAdd.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                FileChooser browser = new FileChooser();
                File selected = browser.showOpenDialog(stage);
                if (selected != null)
                {
                    String fileName = selected.getPath();
                    String converted = EDUS2View.convertFileName(fileName);
                    boolean added = false;
                    while (!added)
                    {
                        TextInputDialog nameEntry = new TextInputDialog();
                        nameEntry.setHeaderText("Enter Scan ID");
                        nameEntry
                                .setContentText("What would you like the scan ID to be?");
                        Optional<String> result = nameEntry.showAndWait();
                        try
                        {
                            if (result.get().equals(""))
                            {
                                Alert alert = new Alert(AlertType.ERROR,
                                        "You must enter a scan ID!");
                                alert.showAndWait();
                            }
                            else
                            {
                                addScan(result.get(), converted);
                                added = true;
                            }
                        }
                        catch (Exception e)
                        {
                            Alert alert = new Alert(AlertType.ERROR,
                                    "You must enter a scan ID!");
                            alert.showAndWait();
                        }
                    }
                }
            }
        });

        // When bulk add is clicked, the user can select multiple files to add.
        // Then we run through each selected file and set ID's for all of them.
        btnBulkAdd.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                FileChooser browser = new FileChooser();
                List<File> selected = browser.showOpenMultipleDialog(stage);
                if (selected != null)
                {
                    Iterator<File> listIt = selected.iterator();
                    while (listIt.hasNext())
                    {
                        File current = (File) listIt.next();
                        String fileName = current.getPath();
                        boolean added = false;
                        while (!added)
                        {
                            String converted = EDUS2View
                                    .convertFileName(fileName);
                            TextInputDialog nameEntry = new TextInputDialog();
                            nameEntry.setHeaderText("Enter Scan ID");
                            nameEntry.setContentText("Filename: "
                                    + current.getName()
                                    + "\nWhat would you like the scan ID to be?");
                            Optional<String> result = nameEntry.showAndWait();
                            // Now add the scan
                            try
                            {
                                if (result.get().equals(""))
                                {
                                    Alert alert = new Alert(AlertType.ERROR,
                                            "You must enter a scan ID!");
                                    alert.showAndWait();
                                }
                                else
                                {
                                    addScan(result.get(), converted);
                                    added = true;
                                }
                            }
                            catch (Exception e)
                            {
                                Alert alert = new Alert(AlertType.ERROR,
                                        "You must enter a scan ID!");
                                alert.showAndWait();
                            }
                        }
                    }
                }
            }
        });

        // Delete the selected scan when the delete button is clicked.
        btnDelete.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                Scan selected = scanList.getSelectedItem();
                scans.remove(selected);
                scanList.removeItem(selected);
                // Lastly, we'll write out the changes to our save file
                try
                {
                    SaveFile.save(scans, "EDUS2Data.bin");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Delete ALL the scans when the delete all button is clicked.
        btnDeleteAll.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                // Show the user a warning, to let them know they're going
                // to permanently remove all scans from the program by doing
                // this
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to remove ALL scans in memory?\nIt is suggested to export all scans before doing this.");
                alert.setHeaderText("Proceed with removing all scans?");
                alert.setTitle("Proceed with removing all scans?");
                alert.showAndWait();
                if (alert.getResult().getText().equals("OK"))
                {
                    // If OK was clicked, we'll nuke all the scans
                    SettingsWindow.this.scans.clear();
                    scanList.removeAllScans();
                    // Lastly, we'll write out the changes to our save file
                    try
                    {
                        SaveFile.save(scans, "EDUS2Data.bin");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Import scans from a file
        btnImport.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                importScans();
            }
        });

        // Export all the current scans to a file
        btnExport.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent event)
            {
                exportScans();
            }
        });

        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(btnAdd, btnBulkAdd, btnDelete,
                btnDeleteAll, btnImport, btnExport);
        this.getChildren().addAll(scanList, buttons);
    }

    /**
     * 
     * Purpose: Set the stage variable of this class, and set up the stage's
     * icon as well.
     * 
     * @param stage
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
        this.stage.getIcons().add(new Image("img/edus2-icon.png"));
    }

    /**
     * 
     * Purpose: Run through the process of importing scans from a file.
     */
    private void importScans()
    {
        FileChooser browser = new FileChooser();
        File selected = browser.showOpenDialog(stage);
        if (selected != null)
        {
            try
            {
                BufferedReader input = new BufferedReader(new FileReader(
                        selected));
                String entireFile = "";
                String currentLine = "";
                while ((currentLine = input.readLine()) != null)
                {
                    entireFile += currentLine + "\n";
                }
                input.close();
                importScansFromCSV(entireFile);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * Purpose: A helper method, to import scans in csv format from a text file.
     * 
     * @param textFile
     *            - the file to read scans from.
     */
    private void importScansFromCSV(String textFile)
    {
        Scanner reader = new Scanner(textFile);
        // If the file has content, and has our import header, then we'll
        // start reading through the file
        if (reader.hasNextLine()
                && reader.nextLine().equals(EDUS2View.IMPORT_MESSAGE))
        {
            // As long as there are more lines in the file, we'll keep importing
            // scans
            while (reader.hasNextLine())
            {
                String currLine = reader.nextLine();
                if (currLine.indexOf(',') > 0)
                {
                    importSingleScan(currLine);
                }
            }
        }
        reader.close();
    }

    /**
     * 
     * Purpose: A helper method to import a single scan from a line in csv
     * format.
     * 
     * @param csvLine
     *            - The line to read.
     */
    private void importSingleScan(String csvLine)
    {
        // Snip up the line to get our values
        String id = csvLine.substring(0, csvLine.indexOf(','));

        String path = csvLine.substring(csvLine.indexOf(',') + 1);

        // Create a scan from the values, and add it to our array
        // Scan toAdd = new Scan(id, path);
        // scans.add(toAdd);
        // scanList.addItem(toAdd);
        addScan(id, path);
    }

    /**
     * 
     * Purpose: Export all scans to a file of the user's choice.
     */
    private void exportScans()
    {
        // Show a file browser, so the user can select a file to save to.
        FileChooser browser = new FileChooser();
        File selected = browser.showSaveDialog(stage);
        if (selected != null)
        {
            try
            {
                PrintWriter output = new PrintWriter(selected);
                // Print out our header first
                output.println(EDUS2View.IMPORT_MESSAGE);
                Iterator<Scan> scanIterator = scans.iterator();
                // Then print each scan object out as CSV as long as there are
                // more scans to read through
                while (scanIterator.hasNext())
                {
                    output.println(scanIterator.next().toCSV());
                    output.flush();
                }
                output.close();
                Alert alert = new Alert(AlertType.CONFIRMATION,
                        "Scans exported successfully!");
                alert.showAndWait();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * Purpose: Determine if the passed in ID already exists in our array.
     * 
     * @param id
     *            - the ID to check
     * @return - true/false, depending if the ID is present.
     */
    private boolean IDAlreadyExists(String id)
    {
        boolean toReturn = false;
        // Loop through all scans until either the ID is found, or we're at the
        // end of the list
        for (int i = 0; i < scans.size() && !toReturn; i++)
        {
            if (scans.get(i).getId().equals(id))
            {
                toReturn = true;
            }
        }
        return toReturn;
    }

    /**
     * 
     * Purpose: Add a scan to the program.
     * 
     * @param id
     *            - the ID of the new scan
     * @param path
     *            - the path of the scan's video
     */
    private void addScan(String id, String path)
    {
        // perform checks to ensure id is valid and unused
        // add scan to scan collection
        if (!id.equals("") && !IDAlreadyExists(id))
        {
            Scan toAdd = new Scan(id, path);
            scans.add(toAdd);
            scanList.addItem(toAdd);

            // Lastly, we'll write out the changes to our save file
            try
            {
                SaveFile.save(scans, "EDUS2Data.bin");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (IDAlreadyExists(id))
        {
            Alert alert = new Alert(AlertType.ERROR,
                    "There's already a scan with that ID!");
            alert.showAndWait();
        }
    }
}
