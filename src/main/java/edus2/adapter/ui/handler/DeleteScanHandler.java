package edus2.adapter.ui.handler;

import edus2.application.ScanFacade;
import edus2.domain.Scan;
import javafx.stage.Stage;

import java.util.List;

public class DeleteScanHandler extends ScanHandler {
    public DeleteScanHandler(ScanFacade scanFacade) {
        super(scanFacade);
    }

    @Override
    public void validateInputs(List<Scan> selectedScans) {
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        for (Scan scan : selectedScans) {
            scanFacade.removeScan(scan);
        }
    }
}
