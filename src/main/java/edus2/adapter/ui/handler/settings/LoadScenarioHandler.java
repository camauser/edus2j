package edus2.adapter.ui.handler.settings;

import edus2.adapter.repository.file.FileScanImportExportRepository;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Scan;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class LoadScenarioHandler extends ScanHandler {
    private final EDUS2Configuration configuration;
    private final FileScanImportExportRepository importExportRepository;

    public LoadScenarioHandler(ScanFacade scanFacade, EDUS2Configuration configuration, FileScanImportExportRepository importExportRepository) {
        super(scanFacade);
        this.configuration = configuration;
        this.importExportRepository = importExportRepository;
    }

    @Override
    protected void validateInputs(List<Scan> selectedScans) {

    }

    @Override
    protected void process(List<Scan> selectedScans, Stage stage) {
        FileChooser browser = new FileChooser();
        browser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Scenario file format (*.scn)", "*.scn"));
        Optional<File> defaultScenarioDirectory = configuration.getDefaultScenarioDirectory();
        defaultScenarioDirectory.filter(File::exists).ifPresent(browser::setInitialDirectory);
        File scanFile = browser.showOpenDialog(stage);
        if (scanFile != null) {
            try {
                importExportRepository.importScansFromFile(scanFile);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Encountered error while loading scenario: %s", e.getMessage()));
            }
        }
    }
}
