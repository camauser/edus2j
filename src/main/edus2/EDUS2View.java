package edus2;/*
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

import java.io.File;
import edus2.logging.LoggerSingleton;

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
    private MediaPlayer player;
    private MediaView vidView;
    private BorderPane main;
    public static final String IMPORT_MESSAGE = "### EDUS2 Scan Import File - Do not edit! ###";
    public static final Font BUTTON_FONT = new Font("Calibri", 18);
    public static final String EDUS2_SAVE_FILE_NAME = "EDUS2Data.bin";
    private static final String INFO_FLAG = "--info";
    private static final String WARNING_FLAG = "--warning";
    private static final String ERROR_FLAG = "--error";
    private static final String NO_INFO_FLAG = "--no-info";
    private static final String NO_WARNING_FLAG = "--no-warning";
    private static final String NO_ERROR_FLAG = "--no-error";
    private ScanFacade scans;

    /**
     * 
     * Purpose: The main method, used to launch our application.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // Run the start method, and open up the GUI
        LoggerSingleton.initializeLogger();
        LoggerSingleton.enableErrorLogging();
        processArguments(args);
        Application.launch(args);
    }

    private static void processArguments(String[] args) {
        for(String arg : args)
        {
            switch(arg){
                case INFO_FLAG:
                    LoggerSingleton.enableInfoLogging();
                    break;
                case WARNING_FLAG:
                    LoggerSingleton.enableWarningLogging();
                    break;
                case ERROR_FLAG:
                    LoggerSingleton.enableErrorLogging();
                    break;
                case NO_INFO_FLAG:
                    LoggerSingleton.disableInfoLogging();
                    break;
                case NO_WARNING_FLAG:
                    LoggerSingleton.disableWarningLogging();
                    break;
                case NO_ERROR_FLAG:
                    LoggerSingleton.disableErrorLogging();
                    break;
            }
        }
    }

    public static Image getThumbnailImage()
    {
        File imageFile = new File("img/edus2-icon.png");
        return new Image("file:///" + imageFile.getAbsolutePath());
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
            LoggerSingleton.logInfoIfEnabled("Attempting to load scans from " + EDUS2_SAVE_FILE_NAME);
            scans = new ScanFacade();
            LegacyUtilities.loadFileAndConvertToCSVIfNeeded(EDUS2_SAVE_FILE_NAME, scans);
            LoggerSingleton.logInfoIfEnabled("Loaded " + scans.scanCount() + " scans from " + EDUS2_SAVE_FILE_NAME);
        }
        catch (Exception e)
        {
            LoggerSingleton.logErrorIfEnabled("Failed to load scans from " + EDUS2_SAVE_FILE_NAME + ". Reason: " + e.getMessage());
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
        stage.getIcons().add(getThumbnailImage());
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
                    LoggerSingleton.logInfoIfEnabled("Scan \"" + currentScan + "\" was entered");
                    if (scans.containsScan(currentScan))
                    {
                        LoggerSingleton.logInfoIfEnabled("Scan \"" + currentScan + "\" exists in the system");
                        if (currentlyPlaying
                                && !currentScan.equals(currentScanPlaying))
                        {
                            currentlyPlaying = false;
                            player.stop();
                        }
                        if (!currentScan.equals(currentScanPlaying))
                        {
                            String scanPath = scans.getScan(currentScan).get().getPath();
                            video = new Media(scanPath);
                            LoggerSingleton.logInfoIfEnabled("Starting to play " + scanPath + " for scan \"" + currentScan + "\"");
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

                            player.setOnEndOfMedia(() -> {
                                player.stop();
                                currentlyPlaying = false;
                                currentScanPlaying = "";
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
                test.getIcons().add(getThumbnailImage());
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
                SettingsWindow scanWindow = new SettingsWindow(scans);
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
        LoggerSingleton.logInfoIfEnabled("Filename \"" + original + "\" converts to \"" + toReturn + original + "\"");
        return toReturn + original;
    }
}
