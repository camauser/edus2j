import logging.LoggerSingleton;

import java.io.*;
import java.util.ArrayList;

public class LegacyUtilities {
    private static boolean isLegacyFile(String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // We'll grab the object in the file, and then return it
            // If we can convert this to an ArrayList, it's a legacy file
            ArrayList<Scan> scans = (ArrayList<Scan>)ois.readObject();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    private static String convertLegacyFileToCSV(String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Scan> scans = (ArrayList<Scan>)ois.readObject();
            EDUS2Logic logic = new EDUS2Logic();
            logic.addMultipleScans(scans);
            return logic.toCSV();
        }
        catch(Exception e)
        {
            LoggerSingleton.logErrorIfEnabled("Unable to convert legacy file to CSV. Error: " + e.getMessage());
            return "";
        }
    }

    public static void loadFileAndConvertToCSVIfNeeded(String fileName, EDUS2Logic logic)
    {
        if (LegacyUtilities.isLegacyFile(fileName))
        {
            LoggerSingleton.logInfoIfEnabled("Legacy save file detected, converting to new CSV format");
            logic.importCSV(LegacyUtilities.convertLegacyFileToCSV(fileName));
            SaveFile.save(logic.toCSV(), fileName);
            LoggerSingleton.logInfoIfEnabled("File successfully converted to CSV format.");
        }
        else
        {
            logic.importCSV(LoadFile.load(fileName));
        }
    }
}
