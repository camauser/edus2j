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
import edus2.adapter.ui.*;
import edus2.adapter.ui.usagereporting.ReportStartupTask;
import edus2.application.usagereporting.UsageReportingService;
import edus2.application.version.ApplicationInfo;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

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
    private ManikinScanEnum currentLocationPlaying = null;
    private static ProgressBar playbackProgress;
    private MediaPlayer player;
    private BorderPane main;
    private static final Font BUTTON_FONT = new Font("Calibri", 18);
    private edus2.application.ScanFacade scanFacade;
    private static Injector injector;
    private EDUS2Configuration configuration;
    private edus2.application.AuthenticationFacade authenticationFacade;
    private ManikinFacade manikinFacade;
    private UsageReportingService usageReportingService;

    public static void main(String[] args) {
        // Run the start method, and open up the GUI
        injector = Guice.createInjector(new EDUS2JModule());
        Application.launch(args);
    }

    public static Image getThumbnailImage() {
        return new Image(Objects.requireNonNull(EDUS2View.class.getClassLoader().getResourceAsStream("edus2-icon.png")));
    }

    /**
     * Purpose: The default start method to start the JavaFX GUI.
     */
    public void start(Stage stage) {
        scanFacade = injector.getInstance(ScanFacade.class);
        configuration = injector.getInstance(EDUS2Configuration.class);
        authenticationFacade = injector.getInstance(AuthenticationFacade.class);
        manikinFacade = injector.getInstance(ManikinFacade.class);
        usageReportingService = injector.getInstance(UsageReportingService.class);

        main = new BorderPane();
        Text txtTitle = new Text("EDUS2J Simulator");
        txtTitle.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 36.0));
        txtTitle.setOnMouseClicked((e) -> showCredits());

        HBox titleBox = new HBox(txtTitle);
        titleBox.setAlignment(Pos.BOTTOM_LEFT);
        VBox playbackPositionBox = generatePlaybackPositionControl();
        HBox controlButtons = generateButtonControls(stage);

        BorderPane bottomControlsPane = new BorderPane();
        bottomControlsPane.setLeft(titleBox);
        bottomControlsPane.setCenter(playbackPositionBox);
        bottomControlsPane.setRight(controlButtons);
        BorderPane.setAlignment(txtTitle, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(playbackPositionBox, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(controlButtons, Pos.BOTTOM_RIGHT);

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

        // Need to set the width after controls have been shown otherwise getWidth returns 0 - set width to center playback position
        titleBox.setMinWidth(controlButtons.getWidth());

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                processScanRequest();
            } else {
                currentScanLocation += event.getText();
            }
        });

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

    private HBox generateButtonControls(Stage stage) {
        Button btnFullscreen = new Button("Toggle Fullscreen");
        btnFullscreen.setFont(BUTTON_FONT);
        Button btnScanSettings = new Button("Scan Settings");
        btnScanSettings.setFont(BUTTON_FONT);
        Button btnManikinSettings = new Button("Manikin Settings");
        btnManikinSettings.setFont(BUTTON_FONT);
        Button btnQuit = new Button("Quit");
        btnQuit.setFont(BUTTON_FONT);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnFullscreen, btnScanSettings, btnManikinSettings, btnQuit);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);

        btnFullscreen.setOnAction(event -> {
            main.requestFocus();
            stage.setFullScreen(!stage.isFullScreen());
        });

        btnScanSettings.setOnAction(event -> {
            main.requestFocus();
            if (isAuthenticated()) {
                EDUS2IconStage scanWindowStage = new EDUS2IconStage();
                ScanSettingsWindow scanSettingsWindow = new ScanSettingsWindow(scanFacade, authenticationFacade, configuration, scanWindowStage);
                Scene scanWindowScene = new Scene(scanSettingsWindow);
                scanWindowStage.setScene(scanWindowScene);
                scanWindowStage.show();
            } else {
                Alert invalidPasswordAlert = new Alert(Alert.AlertType.ERROR);
                invalidPasswordAlert.setTitle("Invalid password");
                invalidPasswordAlert.setContentText("Invalid password entered.");
                invalidPasswordAlert.showAndWait();
            }
        });

        btnManikinSettings.setOnAction(event -> {
            main.requestFocus();
            EDUS2IconStage manikinSettingStage = new EDUS2IconStage();
            ManikinSettingsWindow manikinSettingsWindow = new ManikinSettingsWindow(manikinFacade, manikinSettingStage);
            Scene manikinWindowScene = new Scene(manikinSettingsWindow);
            manikinSettingStage.setScene(manikinWindowScene);
            manikinSettingStage.show();
        });

        btnQuit.setOnAction(event -> {
            main.requestFocus();
            stage.close();
        });

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

    private VBox generatePlaybackPositionControl() {
        VBox playbackElements = new VBox();
        Text txtPlaybackPosition = new Text("Playback Position");
        txtPlaybackPosition.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 20.0));
        playbackProgress = new ProgressBar(0.0);
        playbackProgress.setMinHeight(18.0);
        playbackProgress.setMinWidth(150.0);
        playbackElements.getChildren().addAll(txtPlaybackPosition, playbackProgress);
        VBox.setMargin(txtPlaybackPosition, new Insets(5.0));
        VBox.setMargin(playbackProgress, new Insets(5.0));
        playbackElements.setAlignment(Pos.BOTTOM_CENTER);
        return playbackElements;
    }

    private void showCredits() {
        BorderPane credits = new BorderPane();
        Text header = new Text("EDUS2J Credits");
        header.setFont(new Font(32.0));
        credits.setTop(header);
        header.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 36.0));
        BorderPane.setAlignment(header, Pos.TOP_CENTER);
        Text details = new Text(format(
                "Credits for this project go to: \n"
                        + "Java Porting: Cameron Auser\n"
                        + "Original Design: Paul Kulyk, Paul Olsynski\n"
                        + "EDUS2 is an emergency department ultrasound simulator, "
                        + "and EDUS2J is a port of this original software to Java.\nEDUS2J version: %s", ApplicationInfo.getVersion()));
        details.setFont(new Font("Calibri", 18.0));
        credits.setCenter(details);
        BorderPane.setAlignment(credits, Pos.CENTER);

        Scene creditScene = new Scene(credits);

        EDUS2IconStage creditStage = new EDUS2IconStage();
        creditStage.setScene(creditScene);
        creditStage.setTitle("EDUS2J Credits");
        creditStage.show();
    }

    private void processScanRequest() {
        Optional<ManikinScanEnum> scanTagLocationOptional = manikinFacade.getScanTagLocation(currentScanLocation);
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
            player.setOnEndOfMedia(() -> currentLocationPlaying = null);
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
