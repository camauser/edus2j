package edus2.adapter.ui.handler.settings;

import com.google.inject.Inject;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BulkAddScanHandler extends AddScanHandler {

    @Inject
    public BulkAddScanHandler(ScanFacade scanFacade, EDUS2Configuration configuration) {
        super(scanFacade, configuration);
    }


    @Override
    public void validateInputs(List<Scan> selectedScans) {
        if (scanFacade.getUnusedScanEnums().isEmpty()) {
            throw new RuntimeException("All manikin scan locations have been linked to scans already!");
        }
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        List<File> selected = promptForScanFiles(stage);
        if (selected == null) {
            return;
        }

        if (selected.size() > scanFacade.getUnusedScanEnums().size()) {
            Set<String> unusedScanEnumNames = scanFacade.getUnusedScanEnums()
                    .stream()
                    .map(ManikinScanEnum::getName)
                    .collect(Collectors.toSet());
            String unusedLocations = String.join(",", unusedScanEnumNames);
            throw new RuntimeException(String.format("You've selected too many videos: you can only link videos to the following locations: %s", unusedLocations));
        }

        for (File current : selected) {
            promptForScanIdAndSaveScan(current);
        }
    }

    protected List<File> promptForScanFiles(Stage stage) {
        FileChooser browser = new FileChooser();
        Optional<File> defaultVideoDirectory = configuration.getDefaultVideoDirectory();
        defaultVideoDirectory.filter(File::exists).ifPresent(browser::setInitialDirectory);
        return browser.showOpenMultipleDialog(stage);
    }
}
