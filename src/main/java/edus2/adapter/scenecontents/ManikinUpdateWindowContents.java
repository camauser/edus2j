package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ManikinFacade;
import edus2.domain.Manikin;
import edus2.domain.ManikinScanEnum;
import javafx.scene.Parent;

public class ManikinUpdateWindowContents extends ManikinEntryWindowContents {

    private final ManikinFacade manikinFacade;
    private boolean manikinBound = false;

    @Inject
    public ManikinUpdateWindowContents(SceneBuilder sceneBuilder, ManikinFacade manikinFacade) {
        super(sceneBuilder);
        this.manikinFacade = manikinFacade;
    }

    public void bindManikin(Manikin manikin) {
        if (manikinBound) {
            clearFields();
        }

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
        manikinBound = true;
    }

    @Override
    protected Parent buildSceneContents() {
        if (!manikinBound) {
            throw new IllegalStateException("Manikin must be bound before update window can be shown!");
        }

        return super.buildSceneContents();
    }

    @Override
    public String getTitle() {
        return "Update Manikin";
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
