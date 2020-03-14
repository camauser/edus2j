package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;

public class ManikinUpdateWindowContents extends ManikinEntryWindowContents {

    private final ManikinFacade manikinFacade;

    @Inject
    public ManikinUpdateWindowContents(SceneBuilder sceneBuilder, ManikinFacade manikinFacade) {
        super(sceneBuilder);
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
