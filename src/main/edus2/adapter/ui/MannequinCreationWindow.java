package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;

public class MannequinCreationWindow extends HBox {
    private GridPane mannequinFieldEntry;
    private final TextField mannequinName;
    private final TextField cardiacSc;
    private final TextField ivc;
    private final TextField ruq;
    private final TextField luq;
    private final TextField abdominalAorta;
    private final TextField pelvis;
    private MannequinFacade mannequinFacade;
    private final TextField rightLungScan;
    private final TextField leftLungScan;
    private final TextField cardiacPslPss;
    private final TextField cardiacA4c;
    private int fieldsAdded = 0;

    public MannequinCreationWindow(MannequinFacade mannequinFacade) {
        super(20.0);
//        this.setPrefColumns(2);
        this.mannequinFieldEntry = new GridPane();
        mannequinFieldEntry.setHgap(10.0);
        mannequinFieldEntry.setVgap(5.0);
        this.mannequinFacade = mannequinFacade;
        File imageFile = new File("img/mannequin-scanpoints.png");
        Image scanPointImage = new Image("file:///" + imageFile.getAbsolutePath());

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
        ImageView imageView = new ImageView(scanPointImage);
        mannequinFieldEntry.setAlignment(Pos.CENTER);
        this.getChildren().addAll(mannequinFieldEntry, imageView);
    }

    private TextField generateEntryBox(String prompt) {
        TextField textField = new TextField();
        mannequinFieldEntry.add(new Text(prompt), 0, fieldsAdded);
        mannequinFieldEntry.add(textField, 1, fieldsAdded);
        fieldsAdded++;
        return textField;
    }
}
