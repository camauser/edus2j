package edus2.adapter.ui.handler.frontpage;

import com.google.inject.Inject;
import edus2.adapter.ui.ListenableMediaPlayer;
import edus2.adapter.ui.Toast;
import edus2.application.ManikinFacade;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinScanEnum;
import edus2.domain.Scan;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.Optional;

import static javafx.scene.media.MediaPlayer.Status.PLAYING;


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
        if (listenableMediaPlayer.getMediaPlayer().isPresent()) {
            MediaPlayer mediaPlayer = listenableMediaPlayer.getMediaPlayer().get();
            switch (mediaPlayer.getStatus()) {
                case PLAYING:
                    mediaPlayer.pause();
                    toast.display(PAUSE_IMAGE);
                    break;
                case PAUSED:
                    mediaPlayer.play();
                    toast.display(PLAY_IMAGE);
                    break;
            }
        }
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
        Media video = new Media(scan.getPath().toFile().toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(video);
        MediaView videoView = new MediaView(mediaPlayer);
        listenableMediaPlayer.setMedia(videoView);
        mainDisplayPane.setCenter(videoView);
    }

    private void stopPlayer() {
        listenableMediaPlayer.getMediaPlayer().ifPresent(MediaPlayer::stop);
    }

    private boolean isScanPlaying(Scan scan) {
        return listenableMediaPlayer.getMediaPlayer()
                .map(mp -> mp.getStatus().equals(PLAYING))
                .orElse(false)
                && scan.getScanEnum().equals(currentLocationPlaying);
    }

    private void registerPlaybackListeners() {
        listenableMediaPlayer.registerListener(ListenableMediaPlayer.ListenableMediaPlayerEventEnum.ON_READY, ((mediaView) -> {
            Media video = mediaView.getMediaPlayer().getMedia();
            mediaView.setPreserveRatio(false);
            double windowWidth = mainDisplayPane.getWidth();
            double windowHeight = mainDisplayPane.getHeight();
            mediaView.setFitWidth(calculateWidth(video.getWidth(), windowWidth));
            mediaView.setFitHeight(calculateHeight(video.getHeight(), windowHeight));
            listenableMediaPlayer.getMediaPlayer().ifPresent(MediaPlayer::play);
        }));

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
