package edus2.adapter.ui;

import edus2.domain.MediaPlayerStatus;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.base.State;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListenableMediaPlayer {
    private final Map<ListenableMediaPlayerEventEnum, Set<MediaPlayerEventHandler>> watcherMap = new HashMap<>();
    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final ImageView videoView;

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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void play(Path path) {
        mediaPlayer.media().play(path.toAbsolutePath().toString());
    }

    public void play() {
        mediaPlayer.controls().play();
    }

    public void pause() {
        mediaPlayer.controls().setPause(true);
    }

    public void stop() {
        mediaPlayer.controls().stop();
    }

    public boolean isPlaying() {
        return getStatus() == MediaPlayerStatus.PLAYING;
    }

    public MediaPlayerStatus getStatus() {
        State state = mediaPlayer.status().state();
        if (state == State.PLAYING) {
            return MediaPlayerStatus.PLAYING;
        } else if (state == State.PAUSED) {
            return MediaPlayerStatus.PAUSED;
        }
        return MediaPlayerStatus.STOPPED;
    }

    public float getPosition() {
        return mediaPlayer.status().position();
    }

    public void clear() {
        stop();
        mediaPlayer.media().reset();
        videoView.setImage(null);
    }

    public void registerListener(ListenableMediaPlayerEventEnum eventType, MediaPlayerEventHandler handler) {
        Set<MediaPlayerEventHandler> watchers = watcherMap.getOrDefault(eventType, new HashSet<>());
        watchers.add(handler);
        watcherMap.put(eventType, watchers);
    }

    public void release() {
        clear();
        mediaPlayer.release();
        mediaPlayerFactory.release();
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
