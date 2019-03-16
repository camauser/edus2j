package edus2.adapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.domain.Scan;
import edus2.application.ScanFacade;
import edus2.adapter.logging.LoggerSingleton;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static edus2.application.Util.Lst;

public class LegacyUtilities {
    private static Gson gson = new GsonBuilder().create();

    private static boolean isLegacyFile(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            // We'll grab the object in the file, and then return it
            // If we can convert this to an ArrayList, it's a legacy file
            ArrayList<Scan> scans = (ArrayList<Scan>) ois.readObject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static List<Scan> readScansFromLegacyFile(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (ArrayList<Scan>) ois.readObject();
        } catch (Exception e) {
            LoggerSingleton.logErrorIfEnabled("Unable to read scans from legacy file: " + e.getMessage());
            return Lst();
        }
    }

    public static List<Scan> loadFileAndConvertToJSONIfNeeded(String fileName) {
        List<Scan> scans;
        if (LegacyUtilities.isLegacyFile(fileName)) {
            LoggerSingleton.logInfoIfEnabled("Legacy save file detected, converting to JSON");
            scans = LegacyUtilities.readScansFromLegacyFile(fileName);
            SaveFile.save(gson.toJson(scans), fileName);
            LoggerSingleton.logInfoIfEnabled("File successfully converted to JSON.");
        } else {
            String fileJson = LoadFile.load(fileName);
            Type scanListType = new TypeToken<List<Scan>>(){}.getType();
            scans = gson.fromJson(fileJson, scanListType);
        }
        return scans;
    }
}
