package edus2.adapter.ui.builder;

import edus2.domain.EDUS2Configuration;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

public class SceneBuilder {

    private EDUS2Configuration configuration;
    private static final String DARK_STYLE_FILE = "css/edus2j-dark.css";
    private List<Scene> createdScenes;

    @Inject
    public SceneBuilder(EDUS2Configuration configuration) {
        this.configuration = configuration;
        createdScenes = new LinkedList<>();
        configuration.registerDarkModeListener(this::onDarkModeConfigToggled);
    }

    private void onDarkModeConfigToggled(boolean darkModeEnabled) {
        if (darkModeEnabled) {
            enableDarkModeOnAll();
        } else {
            disableDarkModeOnAll();
        }
    }

    private void enableDarkModeOnAll() {
        for (Scene scene : createdScenes) {
            scene.getStylesheets().add(SceneBuilder.class.getClassLoader().getResource(DARK_STYLE_FILE).toExternalForm());
        }
    }

    private void disableDarkModeOnAll() {
        for (Scene scene : createdScenes) {
            scene.getStylesheets().remove(SceneBuilder.class.getClassLoader().getResource(DARK_STYLE_FILE).toExternalForm());
        }
    }

    public Scene build(Parent root) {
        Scene scene = new Scene(root);
        if (configuration.darkModeEnabled()) {
            scene.getStylesheets().add(SceneBuilder.class.getClassLoader().getResource(DARK_STYLE_FILE).toExternalForm());
        }

        createdScenes.add(scene);
        return scene;
    }
}
