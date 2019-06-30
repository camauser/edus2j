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

import edus2.application.EDUS2View;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.MediaPlayer;

/**
 * 
 * Purpose: A thread used to update the progress of the video currently being
 * played in the EDUS2J software.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class ProgressUpdater extends Thread
{
    private MediaPlayer mediaPlayer;
    private double videoLength;
    private double currentTime;
    private ProgressBar progressBar;
    private EDUS2View edus2View;

    /**
     * 
     * Constructor for the ProgressUpdater class.
     * 
     * @param mediaPlayer
     *            - The MediaPlayer that we're going to keep track of to update
     *            the progress bar
     * @param progressBar
     *            - The progress bar that we want to update
     * @param edus2View
     *            - A reference to our instance of EDUS2View
     */
    public ProgressUpdater(MediaPlayer mediaPlayer, ProgressBar progressBar,
            EDUS2View edus2View)
    {
        this.mediaPlayer = mediaPlayer;
        this.progressBar = progressBar;
        videoLength = mediaPlayer.getTotalDuration().toSeconds();
        this.edus2View = edus2View;
    }

    /**
     * Purpose: The default run method.
     */
    public void run()
    {
        currentTime = 0;
        // Simply loop through this code as long as the video is playing,
        // and update the progress through the video
        while (currentTime < videoLength && edus2View.isCurrentlyPlaying())
        {
            currentTime = mediaPlayer.getCurrentTime().toSeconds();
            Platform.runLater(() -> {
                progressBar.setProgress(currentTime / videoLength);
            });
            try
            {
                Thread.sleep(30);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        edus2View.durationThreads--;
    }
}
