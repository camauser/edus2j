package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileScanRepository  extends FileRepository implements ScanRepository {
    private static final String DEFAULT_SCAN_FILE = "EDUS2Data.json";
    private final Gson gson;
    private EDUS2Configuration configuration;

    @Inject
    public FileScanRepository(EDUS2Configuration configuration) {
        this.configuration = configuration;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public List<Scan> retrieveAll() {
        List<Scan> scans = new ArrayList<>();
        Optional<String> jsonOptional = readFileContents(getScanFile());
        if (!jsonOptional.isPresent()) {
            return scans;
        }

        Type type = new TypeToken<List<Scan>>(){}.getType();
        List<Scan> retrievedScans = gson.fromJson(jsonOptional.get(), type);
        scans.addAll(retrievedScans);
        return scans;
    }

    @Override
    public void save(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.add(scan);
        String jsonScanString = gson.toJson(allScans);
        saveToFile(jsonScanString, getScanFile());
    }

    @Override
    public void remove(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.remove(scan);
        String jsonScanString = gson.toJson(allScans);
        saveToFile(jsonScanString, getScanFile());
    }

    @Override
    public void removeAll() {
        List<Scan> noScans = new ArrayList<>();
        String jsonScanString = gson.toJson(noScans);
        saveToFile(jsonScanString, getScanFile());
    }

    private String getScanFile() {
        return configuration.getScanFileLocation().orElse(DEFAULT_SCAN_FILE);
    }
}
