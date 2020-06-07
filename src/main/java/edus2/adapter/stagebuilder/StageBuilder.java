package edus2.adapter.stagebuilder;

import edus2.adapter.ui.EDUS2IconStage;
import edus2.adapter.ui.builder.SceneBuilder;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class StageBuilder {
    private final SceneBuilder sceneBuilder;

    public StageBuilder(SceneBuilder sceneBuilder) {
        this.sceneBuilder = sceneBuilder;
    }

    public EDUS2IconStage build() {
        EDUS2IconStage stage = new EDUS2IconStage();
        stage.setScene(buildScene());
        stage.setTitle(getTitle());
        return stage;
    }

    protected Scene buildScene() {
        return sceneBuilder.build(buildSceneContents());
    }

    protected abstract Parent buildSceneContents();

    public abstract String getTitle();
}
