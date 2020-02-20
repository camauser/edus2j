package edus2.adapter.ui.handler.settings;

import edus2.application.ScanFacade;
import edus2.domain.Scan;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class EditScanFileHandler extends ScanHandler {

    public EditScanFileHandler(ScanFacade scanFacade) {
        super(scanFacade);
    }

    @Override
    public void validateInputs(List<Scan> selectedScans) {
        if (selectedScans.size() > 1) {
            throw new RuntimeException("Only one video path can be changed at a time!");
        }
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        if (selectedScans.isEmpty()) {
            return;
        }

        Scan selectedScan = selectedScans.get(0);

        FileChooser browser = new FileChooser();
        File selected = browser.showOpenDialog(stage);
        if (selected != null) {
            String convertedFilePath = convertFilePath(selected.getPath());
            scanFacade.removeScan(selectedScan);
            addScan(selectedScan.getScanEnum(), convertedFilePath);
        }
    }
}
