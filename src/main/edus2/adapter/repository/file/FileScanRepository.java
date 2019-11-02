package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.adapter.logging.LoggerSingleton;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileScanRepository implements ScanRepository {
    private final Gson gson;
    private String fileName;

    @Inject
    public FileScanRepository(String fileName) {
        this.fileName = fileName;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public List<Scan> retrieveAll() {

        List<Scan> scans = new ArrayList<>();
        Optional<String> jsonOptional = readFileContents();
        if (!jsonOptional.isPresent()) {
            return scans;
        }

        Type type = new TypeToken<List<Scan>>(){}.getType();
        List<Scan> retrievedScans = gson.fromJson(jsonOptional.get(), type);
        scans.addAll(retrievedScans);
        return scans;
    }

    private Optional<String> readFileContents() {
        File file = new File(fileName);
        StringBuilder json = new StringBuilder();
        try {
            file.createNewFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    json.append(currentLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json.toString().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(json.toString());
    }

    @Override
    public void save(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.add(scan);
        String jsonScanString = gson.toJson(allScans);
        saveToFile(jsonScanString);
    }

    @Override
    public void remove(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.remove(scan);
        String jsonScanString = gson.toJson(allScans);
        saveToFile(jsonScanString);
    }

    @Override
    public void removeAll() {
        List<Scan> noScans = new ArrayList<>();
        String jsonScanString = gson.toJson(noScans);
        saveToFile(jsonScanString);
    }

    private void saveToFile(String content) {
        File file = new File(fileName);
        try {
            file.createNewFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                writer.close();
            }
        } catch (IOException e) {
            LoggerSingleton.logErrorIfEnabled("Unable to save to " + fileName + ". Error: " + e.getMessage());
        }
    }
}
