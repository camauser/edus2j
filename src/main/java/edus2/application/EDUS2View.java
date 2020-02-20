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
import edus2.adapter.ui.handler.frontpage.ScanPlaybackHandler;
import edus2.adapter.ui.usagereporting.ReportStartupTask;
import edus2.application.usagereporting.UsageReportingService;
import edus2.domain.EDUS2Configuration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Purpose: The main class used to run the EDUS2J program.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class EDUS2View extends Application {
    private static final String DEFAULT_BACKGROUND_COLOR_HEX = "#575957";
    private static final String BACKGROUND_COLOR_CSS_STYLE = String.format("-fx-background-color: %s", DEFAULT_BACKGROUND_COLOR_HEX);
    private ListenableMediaPlayer listenablePlayer = new ListenableMediaPlayer();
    private EDUS2Configuration configuration;
    private UsageReportingService usageReportingService;

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
        configuration = injector.getInstance(EDUS2Configuration.class);
        usageReportingService = injector.getInstance(UsageReportingService.class);
        ScanPlaybackHandler scanPlaybackHandler = injector.getInstance(ScanPlaybackHandler.class);

        main.setStyle(BACKGROUND_COLOR_CSS_STYLE);
        BorderPane bottomControlsPane = injector.getInstance(MainControlsPane.class);
        StackPane controlOverlayPane = new StackPane();
        controlOverlayPane.getChildren().addAll(main, bottomControlsPane);

        Scene scene = new Scene(controlOverlayPane);
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
        ensurePhoneHomeWarningAccepted(stage);
        reportStartupToServer();
    }

    private void reportStartupToServer() {
        ReportStartupTask task = new ReportStartupTask(usageReportingService);
        task.setOnSucceeded(wse -> {
            Optional<String> serverResponseMessage = task.getValue();
            if (serverResponseMessage.isPresent()) {
                Alert serverMessage = new Alert(Alert.AlertType.INFORMATION, serverResponseMessage.get());
                serverMessage.setTitle("EDUS2J Information");
                serverMessage.setHeaderText("EDUS2J Information");
                serverMessage.showAndWait();
            }
        });

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(task);
        executorService.shutdown();
    }

    private void ensurePhoneHomeWarningAccepted(Stage stage) {
        if (!configuration.acceptedPhoneHomeWarning()) {
            Alert phoneHomeAlert = new Alert(Alert.AlertType.INFORMATION, "Please note, as a means of tracking" +
                    " uptake/dissemination/impact, we are collecting metrics on downloads and usage of the software." +
                    " This information/data may be collated and shared in aggregate form with our stakeholders." +
                    " If you do not agree to these terms, please close the application now and do not click \"OK\".");
            phoneHomeAlert.setHeaderText("Data Collection Warning");
            phoneHomeAlert.setTitle("Data Collection Warning");
            Optional<ButtonType> response = phoneHomeAlert.showAndWait();
            boolean warningAccepted = response.isPresent();
            if (warningAccepted) {
                configuration.acceptPhoneHomeWarning();
            } else {
                stage.close();
            }
        }
    }
}
