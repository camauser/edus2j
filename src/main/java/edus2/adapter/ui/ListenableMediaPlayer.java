package edus2.adapter.ui;

import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListenableMediaPlayer {
    private MediaPlayer mediaPlayer;
    private Map<ListenableMediaPlayerEventEnum, Set<MediaPlayerEventHandler>> watcherMap;

    public ListenableMediaPlayer() {
        watcherMap = new HashMap<>();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        registerInternalListeners();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
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
        Set<MediaPlayerEventHandler> listeners = watcherMap.getOrDefault(status, new HashSet<>());
        for (MediaPlayerEventHandler handler : listeners) {
            handler.handleEvent();
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
        ON_STOPPED
    }
}
