package edus2.adapter.scenecontents;

import edus2.adapter.ui.builder.SceneBuilder;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Objects;

public abstract class ManikinEntryWindowContents extends SceneContents {

    private GridPane manikinFieldEntry;
    private TextField manikinName;
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
    private int fieldsAdded = 0;
    private ImageView imageView;

    public ManikinEntryWindowContents(SceneBuilder sceneBuilder) {
        super(sceneBuilder);
    }

    @Override
    protected Parent buildSceneContents() {
        HBox entryWindow = new HBox(20);
        this.manikinFieldEntry = new GridPane();
        manikinFieldEntry.setHgap(10.0);
        manikinFieldEntry.setVgap(5.0);

        generateInputFields();
        Image scanPointImage = new Image(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("manikin-scanpoints.png")));
        imageView = new ImageView(scanPointImage);

        HBox imageHbox = new HBox(imageView);
        imageHbox.setAlignment(Pos.CENTER);
        manikinFieldEntry.setAlignment(Pos.CENTER);
        entryWindow.getChildren().addAll(manikinFieldEntry, imageHbox);
        entryWindow.setAlignment(Pos.CENTER);
        return entryWindow;
    }

    public void bindManikin(Manikin manikin) {
        clearFields();
        manikinName.setText(manikin.getName());
        manikinName.setEditable(false);
        rightLungScan.setText(manikin.getTagMap().get(ManikinScanEnum.RIGHT_LUNG));
        leftLungScan.setText(manikin.getTagMap().get(ManikinScanEnum.LEFT_LUNG));
        cardiacPslPss.setText(manikin.getTagMap().get(ManikinScanEnum.CARDIAC_PSL_PSS));
        cardiacA4c.setText(manikin.getTagMap().get(ManikinScanEnum.CARDIAC_A4C));
        cardiacSc.setText(manikin.getTagMap().get(ManikinScanEnum.CARDIAC_SC));
        ivc.setText(manikin.getTagMap().get(ManikinScanEnum.IVC));
        ruq.setText(manikin.getTagMap().get(ManikinScanEnum.RUQ));
        luq.setText(manikin.getTagMap().get(ManikinScanEnum.LUQ));
        abdominalAorta.setText(manikin.getTagMap().get(ManikinScanEnum.ABDOMINAL_AORTA));
        pelvis.setText(manikin.getTagMap().get(ManikinScanEnum.PELVIS));
    }

    private void generateInputFields() {
        manikinName = generateEntryBox("Manikin Name");
        leftLungScan = generateEntryBox("(1) Left Lung Scan");
        rightLungScan = generateEntryBox("(2) Right Lung Scan");
        cardiacPslPss = generateEntryBox("(3) Cardiac PSL/PSS Scan");
        cardiacA4c = generateEntryBox("(4) Cardiac A4C");
        cardiacSc = generateEntryBox("(5) Cardiac SC");
        ivc = generateEntryBox("(6) IVC");
        luq = generateEntryBox("(7) Left Upper Quadrant");
        ruq = generateEntryBox("(8) Right Upper Quadrant");
        abdominalAorta = generateEntryBox("(9) Abdominal Aorta");
        pelvis = generateEntryBox("(10) Pelvis");
        Button btnSaveManikin = new Button("Save Manikin");
        btnSaveManikin.setOnAction(action -> {
            try {
                Manikin manikin = generateManikin();
                saveManikin(manikin);
                if (shouldClearFieldsAfterSave()) {
                    clearFields();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("Successfully saved manikin '%s'", manikin.getName()));
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, String.format("Error saving manikin: %s", e.getMessage()));
                alert.showAndWait();
            }

        });
        manikinFieldEntry.add(btnSaveManikin, 0, fieldsAdded);
        fieldsAdded++;
    }

    protected abstract void saveManikin(Manikin manikin);

    protected abstract boolean shouldClearFieldsAfterSave();

    public void handleWindowResize(int newWidth) {
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(Math.min(imageView.getImage().getWidth(), newWidth / 2));
    }

    private Manikin generateManikin() {
        HashMap<ManikinScanEnum, String> tagMap = new HashMap<>();
        tagMap.put(ManikinScanEnum.RIGHT_LUNG, rightLungScan.getText());
        tagMap.put(ManikinScanEnum.LEFT_LUNG, leftLungScan.getText());
        tagMap.put(ManikinScanEnum.CARDIAC_PSL_PSS, cardiacPslPss.getText());
        tagMap.put(ManikinScanEnum.CARDIAC_A4C, cardiacA4c.getText());
        tagMap.put(ManikinScanEnum.CARDIAC_SC, cardiacSc.getText());
        tagMap.put(ManikinScanEnum.IVC, ivc.getText());
        tagMap.put(ManikinScanEnum.RUQ, ruq.getText());
        tagMap.put(ManikinScanEnum.LUQ, luq.getText());
        tagMap.put(ManikinScanEnum.ABDOMINAL_AORTA, abdominalAorta.getText());
        tagMap.put(ManikinScanEnum.PELVIS, pelvis.getText());
        return new Manikin(tagMap, manikinName.getText());
    }

    private void clearFields() {
        fieldsAdded = 0;
        manikinFieldEntry.getChildren().clear();
        generateInputFields();
    }

    private TextField generateEntryBox(String prompt) {
        TextField textField = new TextField();
        manikinFieldEntry.add(new Text(prompt), 0, fieldsAdded);
        manikinFieldEntry.add(textField, 1, fieldsAdded);
        fieldsAdded++;
        return textField;
    }
}
