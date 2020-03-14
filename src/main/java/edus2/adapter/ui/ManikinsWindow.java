package edus2.adapter.ui;

import com.google.inject.Inject;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class ManikinsWindow extends HBox {
    private TableView<Manikin> records;
    private ManikinFacade manikinFacade;

    @Inject
    public ManikinsWindow(ManikinFacade manikinFacade) {
        this.manikinFacade = manikinFacade;
        // Set up a TableView to display all the records in, and then
        // show the table
        records = new TableView<>();
        records.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Manikin, String> manikinName = new TableColumn<>("Manikin Name");
        manikinName.setMinWidth(200);

        manikinName.setCellValueFactory(new PropertyValueFactory<>("name"));

        refreshTableItems();
        //noinspection unchecked
        records.getColumns().addAll(manikinName);

        this.getChildren().add(records);
    }

    public void refreshTableItems() {
        records.setItems(FXCollections.observableArrayList(manikinFacade.getAllManikins()));
    }

    public Manikin getSelectedItem() {
        return records.getSelectionModel().getSelectedItem();
    }
}
