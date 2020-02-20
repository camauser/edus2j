package edus2.adapter.ui.handler.settings;

import edus2.application.ScanFacade;
import edus2.domain.Scan;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public class DeleteAllScanHandler extends ScanHandler {
    public DeleteAllScanHandler(ScanFacade scanFacade) {
        super(scanFacade);
    }

    @Override
    protected void validateInputs(List<Scan> selectedScans) {
    }

    @Override
    protected void process(List<Scan> selectedScans, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("It is suggested to export all scans before doing this.\nAre you sure you want to remove ALL scans?\n");
        alert.setHeaderText("Proceed with removing all scans?");
        alert.setTitle("Proceed with removing all scans?");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            scanFacade.removeAllScans();
        }
    }
}
