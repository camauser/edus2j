package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;

public class ManikinCreateWindowContents extends ManikinEntryWindowContents {

    private final ManikinFacade manikinFacade;

    @Inject
    public ManikinCreateWindowContents(SceneBuilder sceneBuilder, ManikinFacade manikinFacade) {
        super(sceneBuilder);
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

    @Override
    public String getTitle() {
        return "Create Manikin";
    }
}
