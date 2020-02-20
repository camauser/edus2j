package edus2.adapter.ui.handler.settings;

import edus2.adapter.repository.file.FileScanImportExportRepository;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Scan;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class SaveScenarioHandler extends ScanHandler {
    private final EDUS2Configuration configuration;
    private final FileScanImportExportRepository importExportRepository;

    public SaveScenarioHandler(ScanFacade scanFacade, EDUS2Configuration configuration, FileScanImportExportRepository importExportRepository) {
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
        File selected = browser.showSaveDialog(stage);
        if (selected != null) {
            try {
                importExportRepository.exportScansToFile(selected);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Scenario saved successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException(String.format("Encountered error while saving scenario: %s", e.getMessage()));
            }
        }
    }
}
