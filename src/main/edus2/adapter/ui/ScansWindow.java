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

import edus2.application.ScanFacade;
import edus2.domain.Scan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * 
 * Purpose: Display all of the scans in a nicely formatted table.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class ScansWindow extends Pane
{
    private TableView<Scan> records;
    private ScanFacade scanFacade;

    public ScansWindow(ScanFacade scanFacade)
    {
        this.scanFacade = scanFacade;
        // Set up a TableView to display all the records in, and then
        // show the table
        records = new TableView<>();
        records.setEditable(true);
        TableColumn<Scan, String> scanID = new TableColumn<>("Scan ID");
        TableColumn<Scan, String> path = new TableColumn<>("File Path");
        scanID.setMinWidth(100);
        path.setMinWidth(500);

        scanID.setCellValueFactory(new PropertyValueFactory<>("id"));
        path.setCellValueFactory(new PropertyValueFactory<>("path"));

        refreshTableItems();
        records.getColumns().addAll(scanID, path);

        this.getChildren().add(records);
    }

    public void refreshTableItems() {
        records.setItems(FXCollections.observableArrayList(scanFacade.getAllScans()));
    }

    /**
     * 
     * Purpose: Return the currently selected scan.
     * 
     * @return
     */
    public Scan getSelectedItem()
    {
        return records.getSelectionModel().getSelectedItem();
    }

}
