package edus2.adapter.ui.builder;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class FormBuilder {
    private GridPane form;
    private int controlsAdded = 0;
    private static final int LABEL_COLUMN_INDEX = 0;
    private static final int CONTROL_COLUMN_INDEX = 1;
    private static final int UNLABELED_CONTROL_COLUMN_INDEX = 0;

    public FormBuilder() {
        form = new GridPane();
        form.setVgap(8.0);
        form.setHgap(20.0);
    }

    public FormBuilder addControl(String labelText, Node control) {
        Text label = new Text(labelText);
        form.add(label, LABEL_COLUMN_INDEX, controlsAdded);
        form.add(control, CONTROL_COLUMN_INDEX, controlsAdded);
        controlsAdded++;
        return this;
    }

    public FormBuilder addUnlabeledControl(Node control) {
        form.add(control, UNLABELED_CONTROL_COLUMN_INDEX, controlsAdded, 2, 1);
        centerAlign(control);
        controlsAdded++;
        return this;
    }

    public GridPane build() {
        form.setPadding(new Insets(5, 10, 5, 10));
        return form;
    }

    private void centerAlign(Node control) {
        GridPane.setHalignment(control, HPos.CENTER);
        GridPane.setValignment(control, VPos.CENTER);
    }
}
