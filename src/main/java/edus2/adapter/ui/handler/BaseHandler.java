package edus2.adapter.ui.handler;

import edus2.domain.Scan;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;

public abstract class BaseHandler {
    protected abstract void validateInputs(List<Scan> selectedScans);

    protected abstract void process(List<Scan> selectedScans, Stage stage);

    public void handle(List<Scan> selectedScans, Stage stage) {
        try {
            validateInputs(selectedScans);
            process(selectedScans, stage);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
}
