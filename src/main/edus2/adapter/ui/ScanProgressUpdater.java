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

public class ScanProgressUpdater extends Thread {
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private boolean stopped = false;
    private static final int FRAMES_PER_SECOND = 60;
    private static final int MILLIS_PER_SECOND = 1000;
    private static final int MILLIS_BETWEEN_FRAMES = MILLIS_PER_SECOND / FRAMES_PER_SECOND;

    public ScanProgressUpdater(MediaPlayer mediaPlayer, ProgressBar progressBar) {
        this.mediaPlayer = mediaPlayer;
        this.progressBar = progressBar;
    }

    public void run() {
        while (isVideoPlaying()) {
            double currentProgress = mediaPlayer.getCurrentTime().toSeconds() / mediaPlayer.getTotalDuration().toSeconds();
            Platform.runLater(() -> progressBar.setProgress(currentProgress));
            try {
                Thread.sleep(MILLIS_BETWEEN_FRAMES);
            } catch (InterruptedException e) {
                finish();
            }
        }
    }

    private boolean isVideoPlaying() {
        try {
            double currentTime = mediaPlayer.getCurrentTime().toSeconds();
            double videoLength = mediaPlayer.getTotalDuration().toSeconds();
            return currentTime < videoLength && !stopped;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public void finish() {
        stopped = true;
    }
}
