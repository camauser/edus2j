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
import edus2.adapter.ui.MannequinSettingsWindow;
import edus2.adapter.ui.PasswordInputDialog;
import edus2.adapter.ui.ScanProgressUpdater;
import edus2.adapter.ui.ScanSettingsWindow;
import edus2.domain.EDUS2Configuration;
import edus2.domain.MannequinScanEnum;
import edus2.domain.Scan;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import java.util.Optional;

/**
 * Purpose: The main class used to run the EDUS2J program.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class EDUS2View extends Application {
    private static final int DEFAULT_MINIMUM_VIDEO_WIDTH_IN_PIXELS = 1280;
    private static final int DEFAULT_MINIMUM_VIDEO_HEIGHT_IN_PIXELS = 720;
    private static final double MAX_VIDEO_TO_SCREEN_SIZE_RATIO = 0.8;
    private String currentScanLocation = "";
    private MannequinScanEnum currentLocationPlaying = null;
    private static ProgressBar playbackProgress;
    private MediaPlayer player;
    private BorderPane main;
    private static final Font BUTTON_FONT = new Font("Calibri", 18);
    private ScanFacade scanFacade;
    private static Injector injector;
    private EDUS2Configuration configuration;
    private AuthenticationFacade authenticationFacade;
    private MannequinFacade mannequinFacade;

    public static void main(String[] args) {
        // Run the start method, and open up the GUI
        injector = Guice.createInjector(new EDUS2JModule());
        Application.launch(args);
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
        configuration = injector.getInstance(EDUS2Configuration.class);
        authenticationFacade = injector.getInstance(AuthenticationFacade.class);
        mannequinFacade = injector.getInstance(MannequinFacade.class);

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
                currentScanLocation += event.getText();
            }
        });
    }

    private Node generateBottom(Stage stage) {
        Button btnAbout = new Button("About");
        btnAbout.setFont(BUTTON_FONT);
        Button btnFullscreen = new Button("Toggle Fullscreen");
        btnFullscreen.setFont(BUTTON_FONT);
        Button btnScanSettings = new Button("Scan Settings");
        btnScanSettings.setFont(BUTTON_FONT);
        Button btnMannequinSettings = new Button("Mannequin Settings");
        btnMannequinSettings.setFont(BUTTON_FONT);
        Button btnQuit = new Button("Quit");
        btnQuit.setFont(BUTTON_FONT);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnAbout, btnFullscreen, btnScanSettings, btnMannequinSettings, btnQuit);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);

        btnAbout.setOnAction(event -> showCredits());

        btnFullscreen.setOnAction(event -> stage.setFullScreen(!stage.isFullScreen()));

        btnScanSettings.setOnAction(event -> {
            if (isAuthenticated()) {
                ScanSettingsWindow scanSettingsWindow = new ScanSettingsWindow(scanFacade, authenticationFacade, configuration);
                Stage scanWindowStage = new Stage();
                Scene scanWindowScene = new Scene(scanSettingsWindow);
                scanWindowStage.setScene(scanWindowScene);

                // Set the stage ref in our settings window so that we
                // can show on-screen pop-ups for adding scans
                scanSettingsWindow.setStage(scanWindowStage);

                scanWindowStage.show();
            } else {
                Alert invalidPasswordAlert = new Alert(Alert.AlertType.ERROR);
                invalidPasswordAlert.setTitle("Invalid password");
                invalidPasswordAlert.setContentText("Invalid password entered.");
                invalidPasswordAlert.showAndWait();
            }

        });

        btnMannequinSettings.setOnAction(event -> {
            MannequinSettingsWindow mannequinSettingsWindow = new MannequinSettingsWindow(mannequinFacade);
            Stage mannequinSettingStage = new Stage();
            mannequinSettingsWindow.setStage(mannequinSettingStage);
            Scene mannequinWindowScene = new Scene(mannequinSettingsWindow);
            mannequinSettingStage.setScene(mannequinWindowScene);
            mannequinSettingStage.show();
        });

        btnQuit.setOnAction(event -> stage.close());

        return buttons;
    }

    private boolean isAuthenticated() {
        if (!authenticationFacade.isAuthenticationEnabled()) {
            return true;
        }

        Optional<String> passwordAttemptOptional = promptForPassword();

        if (!passwordAttemptOptional.isPresent()) {
            return false;
        }
        return authenticationFacade.isValidLogin(passwordAttemptOptional.get());
    }

    private Optional<String> promptForPassword() {
        PasswordInputDialog passwordEntryBox = new PasswordInputDialog("Enter Password", "Please enter password to continue");
        return passwordEntryBox.showAndWait();
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
        Optional<MannequinScanEnum> scanTagLocationOptional = mannequinFacade.getScanTagLocation(currentScanLocation);
        if (!scanTagLocationOptional.isPresent()) {
            currentScanLocation = "";
            return;
        }

        Optional<Scan> scanOptional = scanFacade.getScan(scanTagLocationOptional.get());
        if (scanOptional.isPresent() && !isScanPlaying(scanOptional.get())) {
            stopPlayer();
            playScan(scanOptional.get());
        }
        currentScanLocation = "";
    }

    private void stopPlayer() {
        if (player != null) {
            player.stop();
        }
    }

    private boolean isScanPlaying(Scan scan) {
        return player != null
                && player.getStatus().equals(MediaPlayer.Status.PLAYING)
                && scan.getScanEnum().equals(currentLocationPlaying);
    }

    private void playScan(Scan scan) {
        stopPlayer();
        String scanPath = scan.getPath();
        currentLocationPlaying = scan.getScanEnum();
        Media video = new Media(scanPath);
        player = new MediaPlayer(video);
        MediaView videoView = new MediaView(player);
        main.setCenter(videoView);

        player.setOnReady(() -> {
            videoView.setPreserveRatio(false);
            int minVideoWidth = configuration.getMinimumVideoWidth().orElse(DEFAULT_MINIMUM_VIDEO_WIDTH_IN_PIXELS);
            int minVideoHeight = configuration.getMinimumVideoHeight().orElse(DEFAULT_MINIMUM_VIDEO_HEIGHT_IN_PIXELS);
            double windowWidth = main.getWidth();
            double windowHeight = main.getHeight();
            videoView.setFitWidth(calculateVideoDimension(minVideoWidth, video.getWidth(), windowWidth));
            videoView.setFitHeight(calculateVideoDimension(minVideoHeight, video.getHeight(), windowHeight));
            ScanProgressUpdater scanProgressUpdater = new ScanProgressUpdater(player, playbackProgress);
            player.setOnEndOfMedia(() -> {
                currentLocationPlaying = null;
            });
            player.setOnStopped(scanProgressUpdater::finish);

            player.play();
            scanProgressUpdater.start();
        });
    }

    private double calculateVideoDimension(int minimumSize, int videoSize, double screenSize) {
        if (videoSize > MAX_VIDEO_TO_SCREEN_SIZE_RATIO * screenSize) {
            return Math.max(minimumSize, MAX_VIDEO_TO_SCREEN_SIZE_RATIO * screenSize);
        } else {
            return Math.max(minimumSize, videoSize);
        }
    }

    public static String convertFilePath(String originalPath) {
        return "file:///" + originalPath.replaceAll("\\\\", "/").replaceAll(" ", "%20");
    }
}
