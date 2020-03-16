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

import com.google.inject.Inject;
import edus2.application.ScanFacade;
import edus2.domain.Scan;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;

public class ScansWindow extends VBox {
    private TableView<Scan> records;
    private ScanFacade scanFacade;

    @Inject
    public ScansWindow(ScanFacade scanFacade) {
        this.scanFacade = scanFacade;
        records = new TableView<>();
        records.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableColumn<Scan, String> scanEnum = new TableColumn<>("Manikin Location");
        scanEnum.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getScanEnum().getName()));
        scanEnum.setMinWidth(130);

        TableColumn<Scan, String> path = new TableColumn<>("File Name");
        path.setCellValueFactory(new PropertyValueFactory<>("path"));
        path.setCellValueFactory(cell -> {
            try {
                return new SimpleStringProperty(Paths.get(new URI(cell.getValue().getPath())).getFileName().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Unknown");
            }
        });
        path.setMinWidth(450);

        refreshTableItems();
        //noinspection unchecked
        records.getColumns().addAll(scanEnum, path);
        records.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(records);
    }

    public void refreshTableItems() {
        records.setItems(FXCollections.observableArrayList(scanFacade.getAllScans()));
    }

    public List<Scan> getSelectedItems() {
        return records.getSelectionModel().getSelectedItems();
    }

}
