package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edus2.adapter.repository.file.dto.ScanDto;
import edus2.domain.EDUS2Configuration;
import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileScanRepository extends FileCentralRepository implements ScanRepository {

    private static final String SCAN_SECTION_NAME = "scans";
    private static final Type SCAN_SECTION_TYPE = new TypeToken<List<ScanDto>>() {
    }.getType();
    private static final String LEGACY_PATH_PREFIX = "file:///";
    private static final String SPACE_ESCAPE_SEQUENCE = "%20";
    private final Gson gson;

    @Inject
    public FileScanRepository(EDUS2Configuration configuration) {
        super(configuration);
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

        List<ScanDto> retrievedScans = gson.fromJson(jsonOptional.get(), SCAN_SECTION_TYPE);
        try {
            retrievedScans.stream().map(ScanDto::toDomain).forEach(scans::add);
        } catch (RuntimeException e) {
            List<Scan> parsedScans = parseLegacyFormatScans(retrievedScans);
            scans.addAll(parsedScans);
        }
        return scans;
    }

    @Override
    public void save(Scan scan) {
        List<ScanDto> dtos = retrieveAll().stream().map(ScanDto::new).collect(Collectors.toList());
        dtos.add(new ScanDto(scan));
        String jsonScanString = gson.toJson(dtos);
        saveSection(jsonScanString);
    }

    @Override
    public void remove(Scan scan) {
        List<Scan> allScans = retrieveAll();
        allScans.remove(scan);
        List<ScanDto> dtos = allScans.stream().map(ScanDto::new).collect(Collectors.toList());
        String jsonScanString = gson.toJson(dtos);
        saveSection(jsonScanString);
    }

    @Override
    public void removeAll() {
        List<Scan> noScans = new ArrayList<>();
        String jsonScanString = gson.toJson(noScans);
        saveSection(jsonScanString);
    }

    private List<Scan> parseLegacyFormatScans(List<ScanDto> dtos) {
        return dtos.stream()
                .map(dto -> {
                    String convertedPath = convertLegacyPathFormat(dto.getPath());
                    return new ScanDto(dto.getScanEnum(), convertedPath);
                })
                .map(ScanDto::toDomain)
                .collect(Collectors.toList());
    }

    private String convertLegacyPathFormat(String legacyPath) {
        if (legacyPath.startsWith(LEGACY_PATH_PREFIX)) {
            legacyPath = legacyPath.substring(LEGACY_PATH_PREFIX.length());
        }

        return legacyPath.replace(SPACE_ESCAPE_SEQUENCE, " ");
    }
}
