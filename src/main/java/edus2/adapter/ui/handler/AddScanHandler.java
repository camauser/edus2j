package edus2.adapter.ui.handler;

import edus2.application.ScanFacade;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class AddScanHandler extends ScanHandler {

    @Inject
    public AddScanHandler(ScanFacade scanFacade) {
        super(scanFacade);
    }

    public void validateInputs(List<Scan> selectedScans) {
        if (scanFacade.getUnusedScanEnums().isEmpty()) {
            throw new RuntimeException("All manikin scan locations have been linked to scans already!");
        }
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        FileChooser browser = new FileChooser();
        File selected = browser.showOpenDialog(stage);
        if (selected != null) {
            promptForScanIdAndSaveScan(selected);
        }
    }

    protected void promptForScanIdAndSaveScan(File file) {
        boolean added = false;
        while (!added) {
            String convertedFilePath = convertFilePath(file.getPath());
            Optional<ManikinScanEnum> scanLocationOptional = promptForScanLocation("Filename: " + file.getName() + "\nWhat location would you like to link the video to?");
            if (!scanLocationOptional.isPresent()) {
                break;
            }

            added = addScan(scanLocationOptional.get(), convertedFilePath);
        }
    }


}
