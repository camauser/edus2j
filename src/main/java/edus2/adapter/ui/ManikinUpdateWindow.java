package edus2.adapter.ui;

import edus2.application.ManikinFacade;
import edus2.domain.Manikin;

public class ManikinUpdateWindow extends ManikinEntryWindow {
    private ManikinFacade manikinFacade;

    public ManikinUpdateWindow(ManikinFacade manikinFacade) {
        super();
        this.manikinFacade = manikinFacade;
    }

    @Override
    protected void saveManikin(Manikin manikin) {
        manikinFacade.update(manikin);
    }

    @Override
    protected boolean shouldClearFieldsAfterSave() {
        return false;
    }
}
