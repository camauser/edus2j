package edus2.adapter.ui;

import edus2.application.MannequinFacade;
import edus2.domain.Mannequin;

public class MannequinUpdateWindow extends MannequinEntryWindow {
    private MannequinFacade mannequinFacade;

    public MannequinUpdateWindow(MannequinFacade mannequinFacade) {
        super();
        this.mannequinFacade = mannequinFacade;
    }

    @Override
    protected void saveMannequin(Mannequin mannequin) {
        mannequinFacade.update(mannequin);
    }

    @Override
    protected boolean shouldClearFieldsAfterSave() {
        return false;
    }
}
