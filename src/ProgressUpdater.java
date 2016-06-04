/*
 * Copyright 2016 Cameron Auser
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * 
 *  Purpose: A thread used to update the progress of the video currently
 *  being played in the EDUS2J software.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class ProgressUpdater extends Thread
{
    private MediaPlayer toWatch;
    private double videoLength;
    private double currentTime;
    private ProgressBar toUpdate;
    private EDUS2View master;

    /**
     * 
     * Constructor for the ProgressUpdater class.
     * @param toWatch - The MediaPlayer that we're going to keep track of to 
     * update the progress bar 
     * @param toUpdate - The progress bar that we want to update
     * @param master - A reference to our instance of EDUS2View
     */
    public ProgressUpdater(MediaPlayer toWatch, ProgressBar toUpdate, EDUS2View master)
    {
        this.toWatch = toWatch;
        this.toUpdate = toUpdate;
        videoLength = toWatch.getTotalDuration().toSeconds();
        this.master = master;
    }

    /**
     * Purpose: The default run method.
     */
    public void run()
    {
        currentTime = 0;
        // Simply loop through this code as long as the video is playing,
        // and update the progress through the video
        while (currentTime < videoLength && master.currentlyPlaying)
        {
            currentTime = toWatch.getCurrentTime().toSeconds();
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    toUpdate.setProgress(currentTime / videoLength);
                }
            });
            try
            {
                Thread.sleep((long) videoLength);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        master.durationThreads--;
    }
}
