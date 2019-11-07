package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileScanRepository  extends FileRepository implements ScanRepository {
    private final Gson gson;

    @Inject
    public FileScanRepository(String fileName) {
        super(fileName);
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
}
