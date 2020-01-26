package edus2.adapter.ui;

import edus2.application.ManikinFacade;
import edus2.domain.Manikin;

public class ManikinCreateWindow extends ManikinEntryWindow {
    private ManikinFacade manikinFacade;

    public ManikinCreateWindow(ManikinFacade manikinFacade) {
        super();
        this.manikinFacade = manikinFacade;
    }

    @Override
    protected void saveManikin(Manikin manikin) {
        manikinFacade.create(manikin);
    }

    @Override
    protected boolean shouldClearFieldsAfterSave() {
        return true;
    }
}
