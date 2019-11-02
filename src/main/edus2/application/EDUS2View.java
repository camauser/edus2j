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
import edus2.adapter.logging.LoggerSingleton;
import edus2.adapter.ui.ScanProgressUpdater;
import edus2.adapter.ui.SettingsWindow;
import edus2.domain.Scan;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

/**
 * Purpose: The main class used to run the EDUS2J program.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class EDUS2View extends Application {
    // TODO: Change import/export file format to be JSON - get rid of CSV import business
    private String currentScan = "";
    private String currentScanPlaying = "";
    private static ProgressBar playbackProgress;
    private MediaPlayer player;
    private BorderPane main;
    public static final String IMPORT_MESSAGE = "### EDUS2 Scan Import File - Do not edit! ###";
    private static final Font BUTTON_FONT = new Font("Calibri", 18);
    public static final String EDUS2_SAVE_FILE_NAME = "EDUS2Data.json";
    private static final String INFO_FLAG = "--info";
    private static final String WARNING_FLAG = "--warning";
    private static final String ERROR_FLAG = "--error";
    private static final String NO_INFO_FLAG = "--no-info";
    private static final String NO_WARNING_FLAG = "--no-warning";
    private static final String NO_ERROR_FLAG = "--no-error";
    private ScanFacade scanFacade;
    private static Injector injector;

    public static void main(String[] args) {
        // Run the start method, and open up the GUI
        injector = Guice.createInjector(new EDUS2JModule());
        LoggerSingleton.initializeLogger();
        LoggerSingleton.enableErrorLogging();
        processArguments(args);
        Application.launch(args);
    }

    private static void processArguments(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case INFO_FLAG:
                    LoggerSingleton.enableInfoLogging();
                    break;
                case WARNING_FLAG:
                    LoggerSingleton.enableWarningLogging();
                    break;
                case ERROR_FLAG:
                    LoggerSingleton.enableErrorLogging();
                    break;
                case NO_INFO_FLAG:
                    LoggerSingleton.disableInfoLogging();
                    break;
                case NO_WARNING_FLAG:
                    LoggerSingleton.disableWarningLogging();
                    break;
                case NO_ERROR_FLAG:
                    LoggerSingleton.disableErrorLogging();
                    break;
            }
        }
    }

    public static Image getThumbnailImage() {
        File imageFile = new File("img/edus2-icon.png");
        return new Image("file:///" + imageFile.getAbsolutePath());
    }

    /**
     * Purpose: The default start method to start the JavaFX GUI.
     */
    public void start(Stage stage) {
        scanFacade = injector.getInstance(ScanFacade.class);

        main = new BorderPane();
        main.setTop(generateTop());
        main.setBottom(generateBottom(stage));

        Scene scene = new Scene(main);
        stage.setHeight(720);
        stage.setWidth(1280);
        stage.setScene(scene);
        stage.getIcons().add(getThumbnailImage());
        stage.setTitle("EDUS2J");
        stage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                processScanRequest();
            } else {
                currentScan += event.getText();
            }
        });
    }

    private Node generateBottom(Stage stage) {
        Button btnAbout = new Button("About");
        btnAbout.setFont(BUTTON_FONT);
        Button btnFullscreen = new Button("Toggle Fullscreen");
        btnFullscreen.setFont(BUTTON_FONT);
        Button btnSettings = new Button("Settings");
        btnSettings.setFont(BUTTON_FONT);
        Button btnQuit = new Button("Quit");
        btnQuit.setFont(BUTTON_FONT);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnAbout, btnFullscreen, btnSettings,
                btnQuit);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);

        btnAbout.setOnAction(event -> showCredits());

        btnFullscreen.setOnAction(event -> stage.setFullScreen(!stage.isFullScreen()));

        btnSettings.setOnAction(event -> {
            SettingsWindow scanWindow = new SettingsWindow(scanFacade);
            Stage scanWindowStage = new Stage();
            Scene scanWindowScene = new Scene(scanWindow);
            scanWindowStage.setScene(scanWindowScene);

            // Set the stage ref in our settings window so that we
            // can show on-screen pop-ups for adding scans
            scanWindow.setStage(scanWindowStage);

            scanWindowStage.show();

        });

        btnQuit.setOnAction(event -> stage.close());

        return buttons;
    }

    private VBox generateTop() {
        Text txtTitle = new Text("EDUS2J Simulator");
        txtTitle.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 36.0));
        AnchorPane top = new AnchorPane();

        VBox topThird = new VBox();
        Text txtPlaybackPosition = new Text("Playback Position");
        txtPlaybackPosition.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 20.0));
        playbackProgress = new ProgressBar();
        playbackProgress.setMinHeight(18.0);
        playbackProgress.setMinWidth(150.0);
        playbackProgress.setProgress(0.0);
        topThird.getChildren().addAll(txtTitle, txtPlaybackPosition,
                playbackProgress);
        VBox.setMargin(txtTitle, new Insets(5.0));
        VBox.setMargin(txtPlaybackPosition, new Insets(5.0));
        VBox.setMargin(playbackProgress, new Insets(5.0));
        topThird.setAlignment(Pos.TOP_RIGHT);

        top.getChildren().add(topThird);
        AnchorPane.setRightAnchor(topThird, 5.0);
        BorderPane.setAlignment(topThird, Pos.TOP_RIGHT);
        return topThird;
    }

    private void showCredits() {
        BorderPane credits = new BorderPane();
        Text header = new Text("EDUS2J Credits");
        header.setFont(new Font(32.0));
        credits.setTop(header);
        header.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 36.0));
        BorderPane.setAlignment(header, Pos.TOP_CENTER);
        Text details = new Text(
                "Credits for this project go to: \n"
                        + "Java Porting: Cameron Auser\n"
                        + "Original Design: Paul Kulyk, Paul Olsynski\n"
                        + "EDUS2 is an emergency department ultrasound simulator, "
                        + "and EDUS2J is a port of this original software to Java.");
        details.setFont(new Font("Calibri", 18.0));
        credits.setCenter(details);
        BorderPane.setAlignment(credits, Pos.CENTER);

        Scene creditScene = new Scene(credits);

        Stage creditStage = new Stage();
        creditStage.getIcons().add(getThumbnailImage());
        creditStage.setScene(creditScene);
        creditStage.setTitle("EDUS2J Credits");
        creditStage.show();
    }

    private void processScanRequest() {
        LoggerSingleton.logInfoIfEnabled("Scan \"" + currentScan + "\" was entered");
        if (scanFacade.getScan(currentScan).isPresent()) {
            LoggerSingleton.logInfoIfEnabled("Scan \"" + currentScan + "\" exists in the system");
            if (!isScanPlaying(scanFacade.getScan(currentScan).get())) {
                stopPlayer();
                playScan(scanFacade.getScan(currentScan).get());
            }
            currentScan = "";
        } else {
            currentScan = "";
        }
    }

    private void stopPlayer() {
        if (player != null) {
            player.stop();
        }
    }

    private boolean isScanPlaying(Scan scan) {
        return player != null
                && player.getStatus().equals(MediaPlayer.Status.PLAYING)
                && scan.getId().equals(currentScanPlaying);
    }

    private void playScan(Scan scan) {
        String scanPath = scan.getPath();
        LoggerSingleton.logInfoIfEnabled("Starting to play " + scanPath + " for scan \"" + scan.getId() + "\"");
        currentScanPlaying = scan.getId();
        Media video = new Media(scanPath);
        player = new MediaPlayer(video);
        MediaView videoView = new MediaView(player);
        videoView.setPreserveRatio(true);
        main.setCenter(videoView);

        player.setOnReady(() -> {
            ScanProgressUpdater scanProgressUpdater = new ScanProgressUpdater(player, playbackProgress);
            player.setOnEndOfMedia(() -> player.stop());
            player.setOnStopped(() -> {
                currentScanPlaying = "";
                scanProgressUpdater.finish();
            });

            player.play();
            scanProgressUpdater.start();
        });
    }

    public static String convertFilePath(String originalPath) {
        return "file:///" + originalPath.replaceAll("\\\\", "/").replaceAll(" ", "%20");
    }
}
