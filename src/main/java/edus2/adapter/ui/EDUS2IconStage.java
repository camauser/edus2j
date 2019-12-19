package edus2.adapter.ui;

import edus2.application.EDUS2View;
import javafx.stage.Stage;

public class EDUS2IconStage extends Stage {
    public EDUS2IconStage() {
        super();
        this.getIcons().add(EDUS2View.getThumbnailImage());
    }
}
