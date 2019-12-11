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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ScansWindow extends Pane {
    private TableView<Scan> records;
    private ScanFacade scanFacade;

    public ScansWindow(ScanFacade scanFacade) {
        this.scanFacade = scanFacade;
        records = new TableView<>();
        TableColumn<Scan, String> scanEnum = new TableColumn<>("Mannequin Location");
        scanEnum.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getScanEnum().getName()));
        scanEnum.setMinWidth(130);

        TableColumn<Scan, String> path = new TableColumn<>("File Path");
        path.setCellValueFactory(new PropertyValueFactory<>("path"));
        path.setMinWidth(450);


        refreshTableItems();
        //noinspection unchecked
        records.getColumns().addAll(scanEnum, path);

        this.getChildren().add(records);
    }

    public void refreshTableItems() {
        records.setItems(FXCollections.observableArrayList(scanFacade.getAllScans()));
    }

    public Scan getSelectedItem() {
        return records.getSelectionModel().getSelectedItem();
    }

}
