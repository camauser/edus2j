package edus2.adapter.ui;

import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ListenableMediaPlayer {
    private MediaPlayer mediaPlayer;
    private Map<ListenableMediaPlayerEventEnum, MediaPlayerEventHandler> watcherMap;

    public ListenableMediaPlayer() {
        watcherMap = new HashMap<>();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        registerInternalListeners();
    }

    public Optional<MediaPlayer> getMediaPlayer() {
        return Optional.ofNullable(mediaPlayer);
    }

    private void registerInternalListeners() {
        mediaPlayer.setOnPlaying(this::mediaPlaying);
        mediaPlayer.setOnEndOfMedia(this::endOfMedia);
        mediaPlayer.setOnReady(this::playerReady);
        mediaPlayer.setOnStopped(this::playerStopped);
    }

    private void mediaPlaying() {
        callListeners(ListenableMediaPlayerEventEnum.ON_PLAYING);
    }

    private void endOfMedia() {
        callListeners(ListenableMediaPlayerEventEnum.ON_END_OF_MEDIA);
    }

    private void playerReady() {
        callListeners(ListenableMediaPlayerEventEnum.ON_READY);
    }

    private void playerStopped() {
        callListeners(ListenableMediaPlayerEventEnum.ON_STOPPED);
    }

    private void callListeners(ListenableMediaPlayerEventEnum status) {
        if (watcherMap.containsKey(status)) {
            watcherMap.get(status).handleEvent(mediaPlayer);
        }
    }

    public void setListener(ListenableMediaPlayerEventEnum eventType, MediaPlayerEventHandler handler) {
        watcherMap.put(eventType, handler);
    }

    public enum ListenableMediaPlayerEventEnum {
        ON_PLAYING,
        ON_END_OF_MEDIA,
        ON_READY,
        ON_STOPPED
    }
}
