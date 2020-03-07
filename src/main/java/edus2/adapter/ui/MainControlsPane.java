package edus2.adapter.ui;

import com.google.inject.Inject;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.adapter.ui.handler.frontpage.FullscreenHandler;
import edus2.adapter.ui.handler.frontpage.ManikinSettingsWindowHandler;
import edus2.adapter.ui.handler.frontpage.ScanSettingsWindowHandler;
import edus2.adapter.ui.handler.frontpage.ShutdownHandler;
import edus2.application.AuthenticationFacade;
import edus2.application.ManikinFacade;
import edus2.application.ScanFacade;
import edus2.application.version.ApplicationInfo;
import edus2.domain.EDUS2Configuration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

import static java.lang.String.format;

public class MainControlsPane extends BorderPane {
    private final BorderPane mainDisplayPane;
    private final HBox titleBox;
    private final HBox controlButtons;
    private FullscreenHandler fullscreenHandler;
    private ScanSettingsWindowHandler scanSettingsWindowHandler;
    private ManikinSettingsWindowHandler manikinSettingsWindowHandler;
    private final ShutdownHandler shutdownHandler;
    private final ScanProgressUpdater scanProgressUpdater;
    private SceneBuilder sceneBuilder;
    private ListenableMediaPlayer listenablePlayer;

    @Inject
    public MainControlsPane(Stage stage, BorderPane mainDisplayPane, AuthenticationFacade authenticationFacade, ScanFacade scanFacade, EDUS2Configuration configuration,
                            ManikinFacade manikinFacade, SceneBuilder sceneBuilder, ListenableMediaPlayer listenablePlayer) {
        this.sceneBuilder = sceneBuilder;
        this.listenablePlayer = listenablePlayer;
        ProgressBar playbackProgress = new ProgressBar(0.0);
        playbackProgress.setMinHeight(18.0);
        playbackProgress.setMinWidth(150.0);

        this.mainDisplayPane = mainDisplayPane;
        fullscreenHandler = new FullscreenHandler(mainDisplayPane, stage);
        scanSettingsWindowHandler = new ScanSettingsWindowHandler(mainDisplayPane, authenticationFacade, scanFacade, sceneBuilder, configuration);
        manikinSettingsWindowHandler = new ManikinSettingsWindowHandler(mainDisplayPane, sceneBuilder, manikinFacade);
        scanProgressUpdater = new ScanProgressUpdater(listenablePlayer, playbackProgress);
        shutdownHandler = new ShutdownHandler(mainDisplayPane, stage, scanProgressUpdater);

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
        scanProgressUpdater.start();
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

        Scene creditScene = sceneBuilder.build(credits);

        EDUS2IconStage creditStage = new EDUS2IconStage();
        creditStage.setScene(creditScene);
        creditStage.setTitle("EDUS2J Credits");
        creditStage.show();
    }
}
