package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.InvalidMannequinNameException;
import edus2.domain.Mannequin;
import edus2.domain.MannequinScanEnum;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

public class MannequinCreationWindow extends HBox {
    private GridPane mannequinFieldEntry;
    private TextField mannequinName;
    private TextField cardiacSc;
    private TextField ivc;
    private TextField ruq;
    private TextField luq;
    private TextField abdominalAorta;
    private TextField pelvis;
    private TextField rightLungScan;
    private TextField leftLungScan;
    private TextField cardiacPslPss;
    private TextField cardiacA4c;
    private MannequinFacade mannequinFacade;
    private int fieldsAdded = 0;

    public MannequinCreationWindow(MannequinFacade mannequinFacade) {
        super(20.0);
        this.mannequinFieldEntry = new GridPane();
        mannequinFieldEntry.setHgap(10.0);
        mannequinFieldEntry.setVgap(5.0);
        this.mannequinFacade = mannequinFacade;
        File imageFile = new File("img/mannequin-scanpoints.png");
        Image scanPointImage = new Image("file:///" + imageFile.getAbsolutePath());

        generateInputFields();
        ImageView imageView = new ImageView(scanPointImage);
        mannequinFieldEntry.setAlignment(Pos.CENTER);
        this.getChildren().addAll(mannequinFieldEntry, imageView);
    }

    public void bindMannequin(String name) {
        clearFields();
        Optional<Mannequin> mannequinOptional = mannequinFacade.getMannequin(name);
        if (!mannequinOptional.isPresent()) {
            throw new InvalidMannequinNameException(String.format("Mannequin '%s' does not exist", name));
        }

        Mannequin mannequin = mannequinOptional.get();
        mannequinName.setText(mannequin.getName());
        mannequinName.setEditable(false);
        rightLungScan.setText(mannequin.getTagMap().get(MannequinScanEnum.RIGHT_LUNG));
        leftLungScan.setText(mannequin.getTagMap().get(MannequinScanEnum.LEFT_LUNG));
        cardiacPslPss.setText(mannequin.getTagMap().get(MannequinScanEnum.CARDIAC_PSL_PSS));
        cardiacA4c.setText(mannequin.getTagMap().get(MannequinScanEnum.CARDIAC_A4C));
        cardiacSc.setText(mannequin.getTagMap().get(MannequinScanEnum.CARDIAC_SC));
        ivc.setText(mannequin.getTagMap().get(MannequinScanEnum.IVC));
        ruq.setText(mannequin.getTagMap().get(MannequinScanEnum.RUQ));
        luq.setText(mannequin.getTagMap().get(MannequinScanEnum.LUQ));
        abdominalAorta.setText(mannequin.getTagMap().get(MannequinScanEnum.ABDOMINAL_AORTA));
        pelvis.setText(mannequin.getTagMap().get(MannequinScanEnum.PELVIS));
    }

    private void generateInputFields() {
        mannequinName = generateEntryBox("Mannequin Name");
        rightLungScan = generateEntryBox("(1) Right Lung Scan");
        leftLungScan = generateEntryBox("(2) Left Lung Scan");
        cardiacPslPss = generateEntryBox("(3) Cardiac PSL/PSS Scan");
        cardiacA4c = generateEntryBox("(4) Cardiac A4C");
        cardiacSc = generateEntryBox("(5) Cardiac SC");
        ivc = generateEntryBox("(6) IVC");
        ruq = generateEntryBox("(7) RUQ");
        luq = generateEntryBox("(8) LUQ");
        abdominalAorta = generateEntryBox("(9) Abdominal Aorta");
        pelvis = generateEntryBox("(10) Pelvis");
        Button btnSaveMannequin = new Button("Save Mannequin");
        btnSaveMannequin.setOnAction(action -> {
            saveMannequin();
        });
        mannequinFieldEntry.add(btnSaveMannequin, 0, fieldsAdded);
        fieldsAdded++;
    }

    private void saveMannequin() {
        HashMap<MannequinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(MannequinScanEnum.RIGHT_LUNG, rightLungScan.getText());
        tagMap.put(MannequinScanEnum.LEFT_LUNG, leftLungScan.getText());
        tagMap.put(MannequinScanEnum.CARDIAC_PSL_PSS, cardiacPslPss.getText());
        tagMap.put(MannequinScanEnum.CARDIAC_A4C, cardiacA4c.getText());
        tagMap.put(MannequinScanEnum.CARDIAC_SC, cardiacSc.getText());
        tagMap.put(MannequinScanEnum.IVC, ivc.getText());
        tagMap.put(MannequinScanEnum.RUQ, ruq.getText());
        tagMap.put(MannequinScanEnum.LUQ, luq.getText());
        tagMap.put(MannequinScanEnum.ABDOMINAL_AORTA, abdominalAorta.getText());
        tagMap.put(MannequinScanEnum.PELVIS, pelvis.getText());
        try {
            Mannequin mannequin = new Mannequin(tagMap, mannequinName.getText());
            mannequinFacade.save(mannequin);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Successfully saved mannequin '%s'", mannequin.getName()));
            alert.showAndWait();
            clearFields();
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Error saving mannequin: %s", e.getMessage()));
            alert.showAndWait();
        }
    }

    private void clearFields() {
        fieldsAdded = 0;
        mannequinFieldEntry.getChildren().clear();
        generateInputFields();
    }

    private TextField generateEntryBox(String prompt) {
        TextField textField = new TextField();
        mannequinFieldEntry.add(new Text(prompt), 0, fieldsAdded);
        mannequinFieldEntry.add(textField, 1, fieldsAdded);
        fieldsAdded++;
        return textField;
    }
}
