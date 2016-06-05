/*
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

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * Purpose: The main class used to run the EDUS2J program.
 *
 * @author Cameron Auser
 * @version 1.0
 */
public class EDUS2View extends Application
{
    private String currentScan = "";
    private String currentScanPlaying = "";
    private Media video;
    public boolean currentlyPlaying = false;
    private static ProgressBar playbackProgress;
    public int durationThreads = 0;
    private ArrayList<Scan> scansAndVideos = new ArrayList<Scan>();
    private MediaPlayer player;
    private MediaView vidView;
    private BorderPane main;
    public static final String IMPORT_MESSAGE = "### EDUS2 Scan Import File - Do not edit! ###";
    public static final Font BUTTON_FONT = new Font("Calibri", 18);

    /**
     * 
     * Purpose: The main method, used to launch our application.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Run the start method, and open up the GUI
        Application.launch(args);
    }

    /**
     * 
     * Purpose: Update the playback progress component on the GUI.
     * 
     * @param progress
     *            - the new progress to be shown
     */
    public static void updateProgress(double progress)
    {
        playbackProgress.setProgress(progress);
    }

    /**
     * Purpose: The default start method to start the JavaFX GUI.
     */
    public void start(Stage stage)
    {
        // When the program first starts, we'll try to open up existing scans
        // from the EDUS2Data.bin file. If that file doesn't exist, nothing
        // will happen.
        try
        {
            scansAndVideos = (ArrayList<Scan>) (LoadFile.load("EDUS2Data.bin"));
        }
        catch (Exception e)
        {
        }

        // Setting up the actual interface of the program
        main = new BorderPane();
        Text txtTitle = new Text("EDUS2J Simulator");
        txtTitle.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 36.0));
        AnchorPane top = new AnchorPane();

        VBox topThird = new VBox();
        Text txtPlaybackPosition = new Text("Playback Position");
        txtPlaybackPosition.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 20.0));
        playbackProgress = new ProgressBar();
        playbackProgress.setMinHeight(18.0);
        playbackProgress.setMinWidth(150.0);
        playbackProgress.setProgress(0.0);
        topThird.getChildren().addAll(txtTitle, txtPlaybackPosition,
                playbackProgress);
        VBox.setMargin(txtTitle, new Insets(5.0));
        VBox.setMargin(txtPlaybackPosition, new Insets(5.0));
        VBox.setMargin(playbackProgress, new Insets(5.0));
        topThird.setAlignment(Pos.TOP_RIGHT);

        top.getChildren().add(topThird);
        AnchorPane.setRightAnchor(topThird, 5.0);
        main.setTop(top);
        BorderPane.setAlignment(topThird, Pos.TOP_RIGHT);

        // Setting up bottom right control buttons
        Button btnAbout = new Button("About");
        btnAbout.setFont(BUTTON_FONT);
        Button btnFullscreen = new Button("Toggle Fullscreen");
        btnFullscreen.setFont(BUTTON_FONT);
        Button btnSettings = new Button("Settings");
        btnSettings.setFont(BUTTON_FONT);
        Button btnQuit = new Button("Quit");
        btnQuit.setFont(BUTTON_FONT);

        HBox buttons = new HBox();
        buttons.getChildren().addAll(btnAbout, btnFullscreen, btnSettings,
                btnQuit);
        buttons.setAlignment(Pos.BOTTOM_RIGHT);

        main.setBottom(buttons);

        Rectangle placeholder = new Rectangle(640, 480);
        placeholder.setFill(Color.TRANSPARENT);
        main.setCenter(placeholder);
        Scene scene = new Scene(main);
        stage.setScene(scene);
        stage.getIcons().add(new Image("img/edus2-icon.png"));
        stage.setTitle("EDUS2J");
        stage.show();

        // Setting up a handler for when a key is pressed
        scene.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
            public void handle(KeyEvent event)
            {
                // If enter was pressed, we'll go through the process of
                // checking to see if a valid scan was detected. If so,
                // we'll start up the corresponding video.
                if (event.getCode() == KeyCode.ENTER)
                {
                    if (scanExists(currentScan))
                    {
                        if (currentlyPlaying
                                && !currentScan.equals(currentScanPlaying))
                        {
                            currentlyPlaying = false;
                            player.stop();
                        }
                        if (!currentScan.equals(currentScanPlaying))
                        {
                            video = new Media(getScanVideoById(currentScan));
                            currentScanPlaying = currentScan;
                            player = new MediaPlayer(video);
                            vidView = new MediaView(player);
                            vidView.setMediaPlayer(player);
                            vidView.setPreserveRatio(true);

                            player.setOnReady(new Runnable()
                            {
                                public void run()
                                {
                                    main.setCenter(vidView);
                                    vidView.setVisible(true);
                                    player.play();
                                    currentlyPlaying = true;

                                    if (durationThreads == 0)
                                    {
                                        Thread durationUpdater = new ProgressUpdater(
                                                player, playbackProgress,
                                                EDUS2View.this);
                                        durationThreads++;
                                        durationUpdater.start();
                                    }
                                }
                            });

                            player.setOnEndOfMedia(new Runnable()
                            {
                                public void run()
                                {
                                    player.stop();
                                    currentlyPlaying = false;
                                    currentScanPlaying = "";
                                }
                            });
                        }
                        currentScan = "";
                    }
                    // If the scan doesn't exist, we'll clear the scanned
                    // in info, so that the user can attempt to scan again
                    else
                    {
                        currentScan = "";
                    }
                }
                // If enter wasn't pressed, we'll just add that character
                // onto our current scan string
                else
                {
                    String textEntered = event.getText();
                    currentScan += textEntered;
                }
            }

                });

        // When the about button is clicked, a little popup will show on-screen
        // with credits for the program
        btnAbout.setOnAction(new EventHandler<ActionEvent>()
                {
            public void handle(ActionEvent event)
            {
                BorderPane credits = new BorderPane();
                Text header = new Text("EDUS2J Credits");
                header.setFont(new Font(32.0));
                credits.setTop(header);
                header.setFont(Font.font("Calibri", FontWeight.BOLD,
                        FontPosture.ITALIC, 36.0));
                BorderPane.setAlignment(header, Pos.TOP_CENTER);
                Text details = new Text(
                        "Credits for this project go to: \n"
                                + "Java Porting: Cameron Auser\n"
                                + "Original Design: Paul Kulyk, Paul Olsynski\n"
                                + "EDUS2 is an emergency department ultrasound simulator, "
                                + "and EDUS2J is a port of this original software to Java.");
                details.setFont(new Font("Calibri", 18.0));
                credits.setCenter(details);
                BorderPane.setAlignment(credits, Pos.CENTER);

                Scene scene = new Scene(credits);

                Stage test = new Stage();
                test.getIcons().add(new Image("img/edus2-icon.png"));
                test.setScene(scene);
                test.setTitle("EDUS2J Credits");
                test.show();
            }
                });

        // The fullscreen button will just toggle fullscreen status of the
        // program
        btnFullscreen.setOnAction(new EventHandler<ActionEvent>()
                {
            public void handle(ActionEvent event)
            {
                // Just set the program to be the opposite of what it's current
                // fullscreen status is
                stage.setFullScreen(!stage.isFullScreen());
            }
                });

        // When the settings button is pressed, we'll create a SettingsWindow
        // object and show it on-screen
        btnSettings.setOnAction(new EventHandler<ActionEvent>()
                {
            public void handle(ActionEvent event)
            {
                SettingsWindow scanWindow = new SettingsWindow(scansAndVideos);
                Stage temp = new Stage();
                Scene tempScene = new Scene(scanWindow);
                temp.setScene(tempScene);

                // Set the stage ref in our settings window so that we
                // can show on-screen pop-ups for adding scans
                scanWindow.setStage(temp);

                temp.show();

            }
                });

        // We'll close the program when the quit button is clicked
        btnQuit.setOnAction(new EventHandler<ActionEvent>()
                {
            public void handle(ActionEvent event)
            {
                stage.close();
            }
                });
    }

    /**
     * 
     * Purpose: Convert a passed in String to a valid file name and location.
     * 
     * @param original
     *            - the original path (presumably grabbed from a FileChooser
     *            object
     * @return - the correctly formatted path
     */
    public static String convertFileName(String original)
    {
        String toReturn = "file:///";
        // Simply go through the string, turning backslashes into slashes,
        // and fixing any spaces
        for (int i = 0; i < original.length(); i++)
        {
            if (original.charAt(i) == '\\')
            {
                String previous = original.substring(0, i);
                String after = original.substring(i + 1);
                original = previous + "/" + after;
            }
            else if (original.charAt(i) == ' ')
            {
                String previous = original.substring(0, i);
                String after = original.substring(i + 1);
                original = previous + "%20" + after;
            }
        }
        return toReturn + original;
    }

    /**
     * 
     * Purpose: Get a video according to the passed in scan ID.
     * 
     * @param id
     *            - the ID of the scan
     * @return - the file location of the video
     */
    private String getScanVideoById(String id)
    {
        boolean found = false;
        Iterator<Scan> iterator = scansAndVideos.iterator();
        String toReturn = null;
        Scan foundScan = null;
        // Just iterate through all our records looking for the scan
        while (iterator.hasNext() && !found)
        {
            Scan temp = (Scan) iterator.next();
            if (temp.getId().equals(id))
            {
                foundScan = temp;
                found = true;
            }
        }
        if (foundScan != null)
        {
            toReturn = foundScan.getPath();
        }
        // Finally, return the scan. If the ID wasn't found, an empty String
        // will be returned
        return toReturn;
    }

    /**
     * 
     * Purpose: Determine if a scan exists from a passed in ID
     * 
     * @param id
     *            - the ID to verify existance of
     * @return - true/false depending on if it exists
     */
    private boolean scanExists(String id)
    {
        boolean found = false;
        Iterator<Scan> iterator = scansAndVideos.iterator();

        // Like getScanVideoById, we just iterate through records
        // until we either hit the end, or we find the matching scan
        while (iterator.hasNext() && !found)
        {
            Scan temp = (Scan) iterator.next();
            if (temp.getId().equals(id))
            {
                found = true;
            }
        }
        return found;
    }

}
