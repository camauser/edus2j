package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;

public class MannequinCreateWindow extends MannequinEntryWindow {
    private MannequinFacade mannequinFacade;

    public MannequinCreateWindow(MannequinFacade mannequinFacade) {
        super();
        this.mannequinFacade = mannequinFacade;
    }

    @Override
    protected void saveMannequin(Mannequin mannequin) {
        mannequinFacade.create(mannequin);
    }

    @Override
    protected boolean shouldClearFieldsAfterSave() {
        return true;
    }
}
