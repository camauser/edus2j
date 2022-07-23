package edus2.adapter.ui.handler.settings;

import edus2.application.ScanFacade;
import edus2.application.exception.EmptyScanIdException;
import edus2.application.exception.ScanAlreadyExistsException;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ScanHandler extends SettingsHandler {

    protected ScanFacade scanFacade;

    public ScanHandler(ScanFacade scanFacade){
        this.scanFacade = scanFacade;
    }

    protected boolean addScan(ManikinScanEnum scanEnum, Path path) {
        Scan toAdd = new Scan(scanEnum, path);
        try {
            scanFacade.addScan(toAdd);
            return true;
        } catch (EmptyScanIdException | ScanAlreadyExistsException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    protected Optional<ManikinScanEnum> promptForScanLocation(String prompt) {
        Set<ManikinScanEnum> availableValues = scanFacade.getUnusedScanEnums();
        ChoiceDialog<String> scanLocationDialog = new ChoiceDialog<>(availableValues.iterator().next().getName(), availableValues.stream().map(ManikinScanEnum::getName).collect(Collectors.toSet()));
        scanLocationDialog.setHeaderText("Choose Scan Location");
        scanLocationDialog.setContentText(prompt);
        return scanLocationDialog.showAndWait().map(ManikinScanEnum::findByName);
    }

}
