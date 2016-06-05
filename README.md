# About edus2j
This project is a clone of the original edus2 (Emergency Department Ultrasound Simulator) software, but ported to Java so that the software is easily usable on Windows. The original edus2 repo can be found [here] (https://github.com/asclepius/edus2).

# edus2j Requirements
For this project to run correctly, your system should meet the following pre-requisities:
- Be running Windows 7 (this software hasn't been tested on newer operating systems, but it *should* work fine under Windows 8, Windows 8.1, and Windows 10). Both 32 and 64 bit installations of Windows should work with this software.
- Have JRE (Java Runtime Environment) installed on your system. I recommend downloading version 8u91 or newer. The latest JRE can be downloaded from [this page] (http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).

# Installing edus2j
To install edus2j, you have two options: Either compile the source code into a .jar file yourself, or [download the pre-compiled .jar file provided on this repository] (https://github.com/cameronauser/edus2j/releases). Downloading the pre-compiled .jar file is the easier route. After you've either downloaded the provided .jar file, or compiled the source code yourself, you simply need to double-click on the .jar file for edus2j to run the software. It should open up and bring you to the main window.

# edus2j Usage
Before you're able to use edus2j, you'll need to add some scans. To do so, follow these instructions:

1. Click on the "Settings" button in the bottom right corner of the screen.

2. A new window will pop up with buttons along the bottom. To add a single scan, click on the "Add" button. A file browser will then open up. Navigate to the location of your video file for the scan, and then click on the "Open" button in the file browser.

3. Another window will then open up, prompting you to enter the scan ID. The scan ID will be the RFID tag linked to the video file, so on this screen you can just scan the RFID tag corresponding to the video, and the video and ID will then be added to your list of scans.

The process for adding many videos at once is very similar to the steps outlined above. The only difference is that instead of clicking on the "Add" button in step one, you'll click on the "Bulk Add" button, and select multiple video files rather than one.

After you've added some scans, close the Settings window, and try scanning an RFID tag that you set up a scan for. The corresponding video should start playing in the main window.

# FAQ
As of now, there are no frequently asked questions. However, if you have a question regarding edus2j, please contact me using [this form] (http://camauser.ca/contact.php) and I'll do my best to answer your question.

# Known Bugs
As of right now, there are no currently known bugs or issues with edus2j. However, if you run into what you suspect is a bug, please open a new issue on this repository with the following information:

- The bug itself (what isn't working correctly?)
- Steps taken leading up to the bug
