package edus2.adapter.ui.handler.settings;

import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class AddScanHandler extends ScanHandler {

    protected final EDUS2Configuration configuration;

    @Inject
    public AddScanHandler(ScanFacade scanFacade, EDUS2Configuration configuration) {
        super(scanFacade);
        this.configuration = configuration;
    }

    @Override
    public void validateInputs(List<Scan> selectedScans) {
        if (scanFacade.getUnusedScanEnums().isEmpty()) {
            throw new RuntimeException("All manikin scan locations have been linked to scans already!");
        }
    }

    @Override
    public void process(List<Scan> selectedScans, Stage stage) {
        File selected = promptForScanFile(stage);
        if (selected != null) {
            promptForScanIdAndSaveScan(selected);
        }
    }

    public File promptForScanFile(Stage stage) {
        FileChooser browser = new FileChooser();
        Optional<File> defaultVideoDirectory = configuration.getDefaultVideoDirectory();
        defaultVideoDirectory.filter(File::exists).ifPresent(browser::setInitialDirectory);
        return browser.showOpenDialog(stage);
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
