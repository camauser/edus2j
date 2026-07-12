package edus2.adapter.ui.handler.frontpage;

import com.google.inject.Inject;
import edus2.adapter.ui.ListenableMediaPlayer;
import edus2.adapter.ui.Toast;
import edus2.application.ManikinFacade;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.MediaPlayerStatus;
import edus2.domain.Scan;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.awt.Dimension;
import java.util.Optional;


public class ScanPlaybackHandler {
    private static final int DEFAULT_MINIMUM_VIDEO_WIDTH_IN_PIXELS = 1280;
    private static final int DEFAULT_MINIMUM_VIDEO_HEIGHT_IN_PIXELS = 720;
    private static final double MAX_VIDEO_TO_SCREEN_SIZE_RATIO = 0.8;
    private final Image PLAY_IMAGE = new Image(ScanPlaybackHandler.class.getResourceAsStream("/img/playback/play-icon.png"));
    private final Image PAUSE_IMAGE = new Image(ScanPlaybackHandler.class.getResourceAsStream("/img/playback/pause-icon.png"));

    private final BorderPane mainDisplayPane;
    private final ListenableMediaPlayer listenableMediaPlayer;
    private ManikinFacade manikinFacade;
    private final ScanFacade scanFacade;
    private EDUS2Configuration configuration;
    private Toast toast;
    private String currentScan;
    private ManikinScanEnum currentLocationPlaying = null;

    @Inject
    public ScanPlaybackHandler(BorderPane mainDisplayPane, ListenableMediaPlayer listenableMediaPlayer, ManikinFacade manikinFacade, ScanFacade scanFacade,
                               EDUS2Configuration configuration, Toast toast) {
        this.mainDisplayPane = mainDisplayPane;
        this.listenableMediaPlayer = listenableMediaPlayer;
        this.manikinFacade = manikinFacade;
        this.scanFacade = scanFacade;
        this.configuration = configuration;
        this.toast = toast;
        StackPane videoContainer = new StackPane(listenableMediaPlayer.getVideoNode());
        videoContainer.setAlignment(Pos.CENTER);
        mainDisplayPane.setCenter(videoContainer);
        registerPlaybackListeners();
        currentScan = "";
    }

    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            processScanRequest();
        } else if (event.getCode() == KeyCode.SPACE) {
            toggleVideoPlayStatus();
        } else {
            currentScan += event.getText();
        }
    }

    private void toggleVideoPlayStatus() {
        MediaPlayerStatus status = listenableMediaPlayer.getStatus();
        listenableMediaPlayer.getMediaPlayer().ifPresent(mediaPlayer -> {
            if (status == MediaPlayerStatus.PLAYING) {
                mediaPlayer.controls().setPause(true);
                toast.display(PAUSE_IMAGE);
            } else if (status == MediaPlayerStatus.PAUSED) {
                mediaPlayer.controls().play();
                toast.display(PLAY_IMAGE);
            }
        });
    }

    private void processScanRequest() {
        Optional<ManikinScanEnum> scanTagLocationOptional = manikinFacade.getScanTagLocation(currentScan);
        if (!scanTagLocationOptional.isPresent()) {
            currentScan = "";
            return;
        }

        Optional<Scan> scanOptional = scanFacade.getScan(scanTagLocationOptional.get());
        if (scanOptional.isPresent() && !isScanPlaying(scanOptional.get())) {
            playScan(scanOptional.get());
        }
        currentScan = "";
    }

    private void playScan(Scan scan) {
        stopPlayer();
        currentLocationPlaying = scan.getScanEnum();
        listenableMediaPlayer.play(scan.getPath());
    }

    private void stopPlayer() {
        listenableMediaPlayer.stop();
    }

    private boolean isScanPlaying(Scan scan) {
        return listenableMediaPlayer.isPlaying()
                && scan.getScanEnum().equals(currentLocationPlaying);
    }

    private void registerPlaybackListeners() {
        listenableMediaPlayer.registerListener(ListenableMediaPlayer.ListenableMediaPlayerEventEnum.ON_READY, (videoView) -> {
            Optional<MediaPlayer> mediaPlayerOptional = listenableMediaPlayer.getMediaPlayer();
            if (!mediaPlayerOptional.isPresent()) {
                return;
            }

            Dimension videoDimension = mediaPlayerOptional.get().video().videoDimension();
            int videoWidth = videoDimension != null ? videoDimension.width : DEFAULT_MINIMUM_VIDEO_WIDTH_IN_PIXELS;
            int videoHeight = videoDimension != null ? videoDimension.height : DEFAULT_MINIMUM_VIDEO_HEIGHT_IN_PIXELS;
            double windowWidth = mainDisplayPane.getWidth();
            double windowHeight = mainDisplayPane.getHeight();
            videoView.setPreserveRatio(false);
            videoView.setFitWidth(calculateWidth(videoWidth, windowWidth));
            videoView.setFitHeight(calculateHeight(videoHeight, windowHeight));
        });

        listenableMediaPlayer.registerListener(ListenableMediaPlayer.ListenableMediaPlayerEventEnum.ON_END_OF_MEDIA, (mp) -> currentLocationPlaying = null);
    }

    private double calculateWidth(int videoWidth, double screenWidth) {
        double desiredWidth = Math.min(videoWidth, MAX_VIDEO_TO_SCREEN_SIZE_RATIO * screenWidth);

        int minimumWidth = configuration.getMinimumVideoWidth().orElse(DEFAULT_MINIMUM_VIDEO_WIDTH_IN_PIXELS);
        Optional<Integer> maximumWidth = configuration.getMaximumVideoWidth();
        desiredWidth = Math.max(minimumWidth, desiredWidth);

        if (maximumWidth.isPresent()) {
            desiredWidth = Math.min(maximumWidth.get(), desiredWidth);
        }

        return desiredWidth;
    }

    private double calculateHeight(int videoHeight, double screenHeight) {
        double desiredHeight = Math.min(videoHeight, MAX_VIDEO_TO_SCREEN_SIZE_RATIO * screenHeight);

        int minimumHeight = configuration.getMinimumVideoHeight().orElse(DEFAULT_MINIMUM_VIDEO_HEIGHT_IN_PIXELS);
        desiredHeight = Math.max(minimumHeight, desiredHeight);
        Optional<Integer> maximumHeight = configuration.getMaximumVideoHeight();

        if (maximumHeight.isPresent()) {
            desiredHeight = Math.min(maximumHeight.get(), desiredHeight);
        }

        return desiredHeight;
    }

}
