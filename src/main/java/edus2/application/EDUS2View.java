package edus2.application;/*
 * Copyright 2016 Paul Kulyk, Paul Olszynski, Cameron Auser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.inject.Guice;
import com.google.inject.Injector;
import edus2.adapter.guice.EDUS2JModule;
import edus2.adapter.ui.ListenableMediaPlayer;
import edus2.adapter.ui.MainControlsPane;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.frontpage.ScanPlaybackHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Purpose: The main class used to run the EDUS2J program.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class EDUS2View extends Application {
    private ListenableMediaPlayer listenablePlayer = new ListenableMediaPlayer();

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static Image getThumbnailImage() {
        return new Image(Objects.requireNonNull(EDUS2View.class.getClassLoader().getResourceAsStream("edus2-icon.png")));
    }

    /**
     * Purpose: The default start method to start the JavaFX GUI.
     */
    public void start(Stage stage) {
        BorderPane main = new BorderPane();
        Injector injector = Guice.createInjector(new EDUS2JModule(stage, main, listenablePlayer));
        ScanPlaybackHandler scanPlaybackHandler = injector.getInstance(ScanPlaybackHandler.class);
        SceneBuilder sceneBuilder = injector.getInstance(SceneBuilder.class);

        BorderPane bottomControlsPane = injector.getInstance(MainControlsPane.class);
        StackPane controlOverlayPane = new StackPane();
        controlOverlayPane.getChildren().addAll(main, bottomControlsPane);

        Scene scene = sceneBuilder.build(controlOverlayPane);
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.setScene(scene);
        stage.getIcons().add(getThumbnailImage());
        stage.setTitle("EDUS2J");
        stage.show();
        stage.setFullScreen(true);

        // Ensure control buttons aren't highlighted
        main.requestFocus();

        scene.setOnKeyPressed(scanPlaybackHandler::handle);
    }

}
