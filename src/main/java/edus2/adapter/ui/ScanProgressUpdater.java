package edus2.adapter.ui;/*
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

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.MediaPlayer;

import java.util.concurrent.atomic.AtomicBoolean;

import static edus2.adapter.ui.ListenableMediaPlayer.ListenableMediaPlayerEventEnum.*;

public class ScanProgressUpdater extends Thread {
    private ListenableMediaPlayer listenableMediaPlayer;
    private ProgressBar progressBar;
    private AtomicBoolean videoPlaying = new AtomicBoolean(false);
    private AtomicBoolean shutdownRequested = new AtomicBoolean(false);
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MILLIS_BETWEEN_FRAMES = MILLIS_PER_SECOND / FRAMES_PER_SECOND;

    public ScanProgressUpdater(ListenableMediaPlayer listenableMediaPlayer, ProgressBar progressBar) {
        this.listenableMediaPlayer = listenableMediaPlayer;
        this.progressBar = progressBar;
        listenableMediaPlayer.registerListener(ON_PLAYING, mediaView -> videoPlaying.set(true));
        listenableMediaPlayer.registerListener(ON_PAUSED, mediaView -> videoPlaying.set(false));
        listenableMediaPlayer.registerListener(ON_STOPPED, mediaView -> videoPlaying.set(false));
        listenableMediaPlayer.registerListener(ON_END_OF_MEDIA, mediaView -> videoPlaying.set(false));
    }

    public void run() {
        while (!shutdownRequested.get()) {
            while (videoPlaying.get() && !shutdownRequested.get()) {
                try {
                    if (listenableMediaPlayer.getMediaPlayer().isPresent()) {
                        MediaPlayer mediaPlayer = listenableMediaPlayer.getMediaPlayer().get();
                        double currentProgress = mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
                        Platform.runLater(() -> progressBar.setProgress(currentProgress));
                    }
                    Thread.sleep(MILLIS_BETWEEN_FRAMES);
                } catch (Exception e) {
                    shutdown();
                }
            }
        }
    }

    private void shutdown() {
        shutdownRequested.set(true);
    }
}
