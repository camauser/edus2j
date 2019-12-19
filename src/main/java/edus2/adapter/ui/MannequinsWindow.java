package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MannequinsWindow extends HBox {
    private TableView<Mannequin> records;
    private MannequinFacade mannequinFacade;

    public MannequinsWindow(MannequinFacade mannequinFacade) {
        this.mannequinFacade = mannequinFacade;
        // Set up a TableView to display all the records in, and then
        // show the table
        records = new TableView<>();
        records.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Mannequin, String> mannequinName = new TableColumn<>("Mannequin Name");
        mannequinName.setMinWidth(200);

        mannequinName.setCellValueFactory(new PropertyValueFactory<>("name"));

        refreshTableItems();
        //noinspection unchecked
        records.getColumns().addAll(mannequinName);

        this.getChildren().add(records);
    }

    public void refreshTableItems() {
        records.setItems(FXCollections.observableArrayList(mannequinFacade.getAllMannequins()));
    }

    public Mannequin getSelectedItem() {
        return records.getSelectionModel().getSelectedItem();
    }
}
