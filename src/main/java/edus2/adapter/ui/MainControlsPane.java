package edus2.adapter.ui;

import com.google.inject.Inject;
import edus2.adapter.scenecontents.Credits;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.frontpage.FullscreenHandler;
import edus2.adapter.ui.handler.frontpage.ManikinSettingsWindowHandler;
import edus2.adapter.ui.handler.frontpage.ScanSettingsWindowHandler;
import edus2.adapter.ui.handler.frontpage.ShutdownHandler;
import edus2.application.AuthenticationFacade;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainControlsPane extends BorderPane {
    private final BorderPane mainDisplayPane;
    private final HBox titleBox;
    private final HBox controlButtons;
    private FullscreenHandler fullscreenHandler;
    private ScanSettingsWindowHandler scanSettingsWindowHandler;
    private ManikinSettingsWindowHandler manikinSettingsWindowHandler;
    private final ShutdownHandler shutdownHandler;
    private final ScanProgressUpdater scanProgressUpdater;
    private ListenableMediaPlayer listenablePlayer;
    private ScheduledExecutorService threadPool;
    private Credits credits;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MILLIS_BETWEEN_FRAMES = MILLIS_PER_SECOND / FRAMES_PER_SECOND;

    @Inject
    public MainControlsPane(Stage stage, BorderPane mainDisplayPane, AuthenticationFacade authenticationFacade, ScanFacade scanFacade, EDUS2Configuration configuration,
                            SceneBuilder sceneBuilder, ListenableMediaPlayer listenablePlayer, ScheduledExecutorService threadPool,
                            Credits credits, ManikinSettingsWindowHandler manikinSettingsWindowHandler) {
        this.listenablePlayer = listenablePlayer;
        this.threadPool = threadPool;
        this.credits = credits;
        this.manikinSettingsWindowHandler = manikinSettingsWindowHandler;
        ProgressBar playbackProgress = new ProgressBar(0.0);
        playbackProgress.setMinHeight(18.0);
        playbackProgress.setMinWidth(150.0);

        this.mainDisplayPane = mainDisplayPane;
        fullscreenHandler = new FullscreenHandler(mainDisplayPane, stage);
        scanSettingsWindowHandler = new ScanSettingsWindowHandler(mainDisplayPane, authenticationFacade, scanFacade, sceneBuilder, configuration);
        scanProgressUpdater = new ScanProgressUpdater(listenablePlayer, playbackProgress);
        shutdownHandler = new ShutdownHandler(mainDisplayPane, stage, threadPool);

        titleBox = generateTitleBox();
        VBox playbackPositionBox = generatePlaybackPositionControl(playbackProgress);
        controlButtons = generateButtonControls(stage);

        this.setLeft(titleBox);
        this.setCenter(playbackPositionBox);
        this.setRight(controlButtons);
        BorderPane.setAlignment(playbackPositionBox, Pos.BOTTOM_CENTER);
        BorderPane.setAlignment(controlButtons, Pos.BOTTOM_RIGHT);

        // Need to set the width after controls have been shown otherwise getWidth returns 0 - set width to center playback position
        titleBox.visibleProperty().addListener((obs, old, newValue) -> titleBox.setMinWidth(controlButtons.getWidth()));

        stage.setOnCloseRequest(e -> shutdownHandler.handle());
    }

    private HBox generateTitleBox() {
        Text txtTitle = new Text("EDUS2J Simulator");
        txtTitle.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 36.0));
        txtTitle.setOnMouseClicked((e) -> showCredits());
        HBox titleBox = new HBox(txtTitle);
        titleBox.setAlignment(Pos.BOTTOM_LEFT);
        return titleBox;
    }

    private VBox generatePlaybackPositionControl(ProgressBar playbackProgress) {
        VBox playbackElements = new VBox();
        Text txtPlaybackPosition = new Text("Playback Position");
        txtPlaybackPosition.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.ITALIC, 20.0));
        MainControlButton btnClearScreen = new MainControlButton("Clear Screen");
        playbackElements.getChildren().addAll(txtPlaybackPosition, playbackProgress);
        VBox.setMargin(txtPlaybackPosition, new Insets(5.0));
        VBox.setMargin(playbackProgress, new Insets(5.0));
        VBox.setMargin(btnClearScreen, new Insets(5.0, 0, 0, 0));
        btnClearScreen.setOnAction(e -> {
            if (mainDisplayPane.getCenter() instanceof MediaView) {
                MediaView mediaView = (MediaView) mainDisplayPane.getCenter();
                mediaView.getMediaPlayer().stop();
                mediaView.setMediaPlayer(null);
                playbackElements.getChildren().remove(btnClearScreen);
            }
        });

        listenablePlayer.registerListener(ListenableMediaPlayer.ListenableMediaPlayerEventEnum.ON_END_OF_MEDIA, (mp) -> playbackElements.getChildren().add(btnClearScreen));
        listenablePlayer.registerListener(ListenableMediaPlayer.ListenableMediaPlayerEventEnum.ON_PLAYING, (mp) -> playbackElements.getChildren().remove(btnClearScreen));
        threadPool.scheduleAtFixedRate(scanProgressUpdater, 0, MILLIS_BETWEEN_FRAMES, TimeUnit.MILLISECONDS);
        playbackElements.setAlignment(Pos.BOTTOM_CENTER);
        return playbackElements;
    }

    private HBox generateButtonControls(Stage stage) {
        MainControlButton btnFullscreen = new MainControlButton("Toggle Fullscreen");
        MainControlButton btnScanSettings = new MainControlButton("Scan Settings");
        MainControlButton btnManikinSettings = new MainControlButton("Manikin Settings");
        MainControlButton btnQuit = new MainControlButton("Quit");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnFullscreen, btnScanSettings, btnManikinSettings, btnQuit);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);


        stage.fullScreenProperty().addListener((obs, old, isFullScreen) -> {
            titleBox.setVisible(!isFullScreen);
            controlButtons.setVisible(!isFullScreen);
        });

        btnFullscreen.setOnAction(e -> fullscreenHandler.handle());
        btnScanSettings.setOnAction(e -> scanSettingsWindowHandler.handle());
        btnManikinSettings.setOnAction(e -> manikinSettingsWindowHandler.handle());
        btnQuit.setOnAction(e -> shutdownHandler.handle());
        return buttons;
    }

    private void showCredits() {
        EDUS2IconStage creditStage = new EDUS2IconStage();
        creditStage.setScene(credits.getScene());
        creditStage.setTitle("EDUS2J Credits");
        creditStage.show();
    }
}
