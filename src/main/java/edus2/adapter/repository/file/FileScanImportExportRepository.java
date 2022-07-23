package edus2.adapter.repository.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import edus2.adapter.repository.file.dto.ScanDto;
import edus2.application.ScanFacade;
import edus2.domain.Scan;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class FileScanImportExportRepository {
    private final ScanFacade scanFacade;
    private final Gson gson;

    @Inject
    public FileScanImportExportRepository(ScanFacade scanFacade) {
        this.scanFacade = scanFacade;
        this.gson = new GsonBuilder().create();
    }

    public void exportScansToFile(File outputFile) throws IOException {
        List<ScanDto> scanDtos = scanFacade.getAllScans().stream().map(ScanDto::new).collect(Collectors.toList());
        Files.write(outputFile.toPath(), gson.toJson(scanDtos).getBytes());
    }

    public void importScansFromFile(File inputFile) throws IOException {
        byte[] fileContents = Files.readAllBytes(inputFile.toPath());
        Type type = new TypeToken<List<ScanDto>>(){}.getType();
        List<ScanDto> scanDtos = gson.fromJson(new String(fileContents), type);
        List<Scan> scans = scanDtos.stream().map(ScanDto::toDomain).collect(Collectors.toList());
        scanFacade.removeAllScans();
        scanFacade.addScans(scans);
    }

}
