package edus2.adapter.scenecontents;

import edus2.adapter.ui.builder.SceneBuilder;
import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class SceneContents {
    private final SceneBuilder sceneBuilder;

    public SceneContents(SceneBuilder sceneBuilder) {
        this.sceneBuilder = sceneBuilder;
    }

    public Scene getScene() {
        return sceneBuilder.build(buildSceneContents());
    }

    protected abstract Parent buildSceneContents();

    public abstract String getTitle();
}
