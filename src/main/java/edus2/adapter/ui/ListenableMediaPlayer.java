package edus2.adapter.ui;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.*;

public class ListenableMediaPlayer {
    private Map<ListenableMediaPlayerEventEnum, Set<MediaPlayerEventHandler>> watcherMap;
    private MediaView mediaView;

    public ListenableMediaPlayer() {
        watcherMap = new HashMap<>();
    }

    public void setMedia(MediaView mediaView) {
        this.mediaView = mediaView;
        registerInternalListeners();
    }

    public Optional<MediaPlayer> getMediaPlayer() {
        if (mediaView == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(mediaView.getMediaPlayer());
    }

    private void registerInternalListeners() {
        mediaView.getMediaPlayer().setOnPlaying(this::mediaPlaying);
        mediaView.getMediaPlayer().setOnEndOfMedia(this::endOfMedia);
        mediaView.getMediaPlayer().setOnReady(this::playerReady);
        mediaView.getMediaPlayer().setOnStopped(this::playerStopped);
        mediaView.getMediaPlayer().setOnPaused(this::playerPaused);
    }

    private void playerPaused() {
        callListeners(ListenableMediaPlayerEventEnum.ON_PAUSED);
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
        Set<MediaPlayerEventHandler> listeners = watcherMap.getOrDefault(status, new HashSet<>());
        for (MediaPlayerEventHandler handler : listeners) {
            handler.handleEvent(mediaView);
        }
    }

    public void registerListener(ListenableMediaPlayerEventEnum eventType, MediaPlayerEventHandler handler) {
        Set<MediaPlayerEventHandler> watchers = watcherMap.getOrDefault(eventType, new HashSet<>());
        watchers.add(handler);
        watcherMap.put(eventType, watchers);
    }

    public enum ListenableMediaPlayerEventEnum {
        ON_PLAYING,
        ON_END_OF_MEDIA,
        ON_READY,
        ON_STOPPED,
        ON_PAUSED
    }
}
