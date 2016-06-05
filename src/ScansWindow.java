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

import java.util.ArrayList;
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
    private ObservableList<Scan> scans;
    private TableView<Scan> records;

    /**
     * 
     * Constructor for the ScansWindow class.
     * 
     * @param scans
     *            - the ArrayList of scans to show in the table.
     */
    public ScansWindow(ArrayList<Scan> scans)
    {
        // Set up a TableView to display all the records in, and then
        // show the table
        this.scans = FXCollections.observableArrayList();
        this.scans.addAll(scans);
        records = new TableView<Scan>();
        records.setEditable(true);
        TableColumn<Scan, String> scanID = new TableColumn<Scan, String>(
                "Scan ID");
        TableColumn<Scan, String> path = new TableColumn<Scan, String>(
                "File Path");
        scanID.setMinWidth(100);
        path.setMinWidth(500);

        scanID.setCellValueFactory(new PropertyValueFactory<Scan, String>("id"));
        path.setCellValueFactory(new PropertyValueFactory<Scan, String>("path"));

        records.setItems(this.scans);

        records.getColumns().addAll(scanID, path);

        this.getChildren().add(records);
    }

    /**
     * 
     * Purpose: Return the currently selected scan.
     * 
     * @return
     */
    public Scan getSelectedItem()
    {
        return (Scan) records.getSelectionModel().getSelectedItem();
    }

    /**
     * 
     * Purpose: Remove a certain scan from our array of scans.
     * 
     * @param toRemove
     *            - the Scan to remove.
     */
    public void removeItem(Scan toRemove)
    {
        scans.remove(toRemove);
    }

    /**
     * 
     * Purpose: Add a scan to our array.
     * 
     * @param toAdd
     *            - the scan to add.
     */
    public void addItem(Scan toAdd)
    {
        scans.add(toAdd);
    }

    /**
     * 
     * Purpose: Remove all scans from our array.
     */
    public void removeAllScans()
    {
        scans.clear();
    }
}
