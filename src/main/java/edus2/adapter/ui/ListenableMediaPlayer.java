package edus2.adapter.ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ListenableMediaPlayer {
    private final Map<ListenableMediaPlayerEventEnum, Set<MediaPlayerEventHandler>> watcherMap = new HashMap<>();
    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final ImageView videoView;
    private final AtomicBoolean mediaLoaded = new AtomicBoolean(false);

    public ListenableMediaPlayer() {
        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        videoView = new ImageView();
        videoView.setPreserveRatio(false);
        mediaPlayer.videoSurface().set(PixelWriterVideoSurface.create(videoView));
        registerInternalListeners();
    }

    public Node getVideoNode() {
        return videoView;
    }

    public ImageView getVideoView() {
        return videoView;
    }

    public Optional<MediaPlayer> getMediaPlayer() {
        if (!mediaLoaded.get()) {
            return Optional.empty();
        }
        return Optional.of(mediaPlayer);
    }

    public void play(String mrl) {
        mediaLoaded.set(true);
        mediaPlayer.media().play(mrl);
    }

    public void stop() {
        mediaPlayer.controls().stop();
    }

    public void clear() {
        stop();
        mediaPlayer.media().reset();
        videoView.setImage(null);
        mediaLoaded.set(false);
    }

    public boolean isPlaying() {
        return mediaLoaded.get() && mediaPlayer.status().isPlaying();
    }

    public void release() {
        clear();
        mediaPlayer.release();
        mediaPlayerFactory.release();
    }

    public void registerListener(ListenableMediaPlayerEventEnum eventType, MediaPlayerEventHandler handler) {
        Set<MediaPlayerEventHandler> watchers = watcherMap.getOrDefault(eventType, new HashSet<>());
        watchers.add(handler);
        watcherMap.put(eventType, watchers);
    }

    private void registerInternalListeners() {
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
                callListeners(ListenableMediaPlayerEventEnum.ON_PLAYING);
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
                callListeners(ListenableMediaPlayerEventEnum.ON_PAUSED);
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
                callListeners(ListenableMediaPlayerEventEnum.ON_STOPPED);
            }

            @Override
            public void finished(MediaPlayer mediaPlayer) {
                mediaLoaded.set(false);
                callListeners(ListenableMediaPlayerEventEnum.ON_END_OF_MEDIA);
            }

            @Override
            public void mediaPlayerReady(MediaPlayer mediaPlayer) {
                callListeners(ListenableMediaPlayerEventEnum.ON_READY);
            }

            @Override
            public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
                if (newCount > 0) {
                    callListeners(ListenableMediaPlayerEventEnum.ON_READY);
                }
            }
        });
    }

    private void callListeners(ListenableMediaPlayerEventEnum status) {
        Platform.runLater(() -> {
            Set<MediaPlayerEventHandler> listeners = watcherMap.getOrDefault(status, new HashSet<>());
            for (MediaPlayerEventHandler handler : listeners) {
                handler.handleEvent(videoView);
            }
        });
    }

    public enum ListenableMediaPlayerEventEnum {
        ON_PLAYING,
        ON_END_OF_MEDIA,
        ON_READY,
        ON_STOPPED,
        ON_PAUSED
    }
}
