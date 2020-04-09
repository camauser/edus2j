package edus2.adapter.ui.handler.settings;

import com.google.inject.Inject;
import edus2.application.ScanFacade;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class EditManikinLocationHandler extends ScanHandler {

    @Inject
    public EditManikinLocationHandler(ScanFacade scanFacade) {
        super(scanFacade);
    }

    @Override
    public void validateInputs(List<Scan> selectedScans) {
        if (selectedScans.size() > 1) {
            throw new RuntimeException("Only one manikin location can be changed at a time!");
        }

        if (!selectedScans.isEmpty() && scanFacade.getUnusedScanEnums().isEmpty()) {
            throw new RuntimeException("All manikin scan locations have been linked to scans already!");
        }
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        if (selectedScans.isEmpty()) {
            return;
        }

        Scan scan = selectedScans.get(0);
        updateScanManikinLocation(scan);
    }

    private void updateScanManikinLocation(Scan scan) {
        boolean added = false;
        while (!added) {
            Optional<ManikinScanEnum> scanLocationOptional = promptForScanLocation("What location would you like to link the video to?");
            if (!scanLocationOptional.isPresent()) {
                break;
            }

            scanFacade.removeScan(scan);
            added = addScan(scanLocationOptional.get(), scan.getPath());
        }
    }
}
