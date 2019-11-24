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

public class FileScanRepository extends FileCentralRepository implements ScanRepository {
    private static final String SCAN_SECTION_NAME = "scans";
    private static final Type SCAN_SECTION_TYPE = new TypeToken<List<Scan>>(){}.getType();
    private static final String DEFAULT_SCAN_FILE = "EDUS2Data.json";
    private final Gson gson;

    @Inject
    public FileScanRepository(EDUS2Configuration configuration) {
        super(configuration.getScanFileLocation().orElse(DEFAULT_SCAN_FILE));
        this.gson = new GsonBuilder().create();
    }

    @Override
    protected String getSectionName() {
        return SCAN_SECTION_NAME;
    }

    @Override
    public List<Scan> retrieveAll() {
        List<Scan> scans = new ArrayList<>();
        Optional<String> jsonOptional = retrieveSection();
        if (!jsonOptional.isPresent()) {
            return scans;
        }

        List<Scan> retrievedScans = gson.fromJson(jsonOptional.get(), SCAN_SECTION_TYPE);
        scans.addAll(retrievedScans);
        return scans;
    }

    @Override
    public void save(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.add(scan);
        String jsonScanString = gson.toJson(allScans);
        saveSection(jsonScanString);
    }

    @Override
    public void remove(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.remove(scan);
        String jsonScanString = gson.toJson(allScans);
        saveSection(jsonScanString);
    }

    @Override
    public void removeAll() {
        List<Scan> noScans = new ArrayList<>();
        String jsonScanString = gson.toJson(noScans);
        saveSection(jsonScanString);
    }
}
